package com.example.ecomerseapplication.Controllers;

import com.example.ecomerseapplication.CompositeIdClasses.PurchaseCartId;
import com.example.ecomerseapplication.DTOs.*;
import com.example.ecomerseapplication.Entities.*;
import com.example.ecomerseapplication.EntityToDTOConverters.ProductDTOMapper;
import com.example.ecomerseapplication.EntityToDTOConverters.PurchaseMapper;
import com.example.ecomerseapplication.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("purchase/")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @Autowired
    SavedPurchaseDetailsService purchaseDetailsService;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerCartService customerCartService;

    @Autowired
    PurchaseCartService purchaseCartService;

    @PostMapping("savedetails")
    public ResponseEntity<String> savePurchaseInformation(@RequestBody SavedPurchaseDetailsDto savedPurchaseDetailsDto,
                                                          long id) {
        Customer customer = customerService.findById(id);

        if (customer == null)
            return ResponseEntity.notFound().build();

        SavedPurchaseDetails purchaseDetails = new SavedPurchaseDetails(savedPurchaseDetailsDto, customer);

        return purchaseDetailsService.saveDetails(purchaseDetails);
    }

    @GetMapping("getdetails")
    public ResponseEntity<SavedPurchaseDetailsDto> getPurchaseInformation(long id) {
        Customer customer = customerService.findById(id);

        if (customer == null)
            return ResponseEntity.notFound().build();

        return purchaseDetailsService.getByCustomer(customer);
    }

    @PostMapping("complete")
    public ResponseEntity<PurchaseResponse> createPurchase(@RequestBody PurchaseRequest purchaseRequest) {

        Customer customer = customerService.findById(purchaseRequest.customerId);

        if (customer==null)
            ResponseEntity.notFound().build();

        Purchase purchase = PurchaseMapper.requestToEntity(purchaseRequest.purchaseDto);

        purchase.setCustomer(customer);

        List<CustomerCart> customerCarts = customerCartService.cartsByCustomer(customer);

        if (customerCarts.isEmpty())
            return ResponseEntity.notFound().build();

        int totalCost = 0;

        for (CustomerCart customerCart:customerCarts) {
            totalCost+=customerCart
                    .getCustomerCartId()
                    .getProduct()
                    .getSalePriceStotinki();
        }

        purchase.setTotalCost(totalCost);

        Purchase managedPurchase = purchaseService.save(purchase);

        PurchaseCartId purchaseCartId;

        List<PurchaseCart> purchaseCarts = new ArrayList<>();

        for (CustomerCart customerCart:customerCarts)
        {
            purchaseCartId = new PurchaseCartId();
            purchaseCartId.setPurchase(managedPurchase);
            purchaseCartId.setProduct(customerCart
                    .getCustomerCartId()
                    .getProduct());

            System.out.println(customerCart.getCustomerCartId().getProduct().getProductName());

            purchaseCarts.add(new PurchaseCart(purchaseCartId,
                    customerCart.getQuantity()));
        }

        purchaseCartService.saveCarts(purchaseCarts);

        List<Product> products = new ArrayList<>();

        for (CustomerCart customerCart:customerCarts) {
            products.add(customerCart.getCustomerCartId().getProduct());
        }

        List<CompactProductResponse> productResponses = products.
                stream()
                .map(ProductDTOMapper::entityToCompactResponse)
                .toList();

        PurchaseResponse purchaseResponse = PurchaseMapper.entityToResponse(purchase);

        purchaseResponse.products = productResponses;

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(purchaseResponse);

    }
}
