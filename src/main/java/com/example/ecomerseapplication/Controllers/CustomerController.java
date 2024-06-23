package com.example.ecomerseapplication.Controllers;

import com.example.ecomerseapplication.DTOs.*;
import com.example.ecomerseapplication.Entities.*;
import com.example.ecomerseapplication.EntityToDTOConverters.CompactPurchaseResponseBuilder;
import com.example.ecomerseapplication.EntityToDTOConverters.CustomerCartResponseBuilder;
import com.example.ecomerseapplication.EntityToDTOConverters.ProductDTOMapper;
import com.example.ecomerseapplication.Others.PageContentLimit;
import com.example.ecomerseapplication.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerCartService customerCartService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PurchaseCartService purchaseCartService;

    @PostMapping("registration")
    public ResponseEntity<String> register(@RequestBody CustomerAccountRequest customerAccountRequest) {

        return ResponseEntity.status(customerService.registration(customerAccountRequest)).build();
    }

    @PostMapping("login")
    public ResponseEntity<Integer> logIn(@RequestBody CustomerAccountRequest customerAccountRequest) {
        return customerService.logIn(customerAccountRequest);
    }

    @PostMapping("addfavourite")
    public ResponseEntity<HttpStatus> addProductToFavourites(@RequestBody CustomerProductPairRequest pairRequest) {
        Product product = productService.findByPCode(pairRequest.productCode);

        if (product == null)
            return ResponseEntity.notFound().build();
        return customerService.addProductToFavourites(pairRequest.customerId, product);
    }

    @GetMapping("favourites/p/{page}")
    public ResponseEntity<CompactProductPagedListDto> getFavourites(@RequestParam long id, @PathVariable int page) {
        Customer customer = customerService.findById(id);
        if (customer == null)
            return ResponseEntity.notFound().build();

        PageRequest pageRequest = PageRequest.of(page, PageContentLimit.limit);

        CompactProductPagedListDto productResponse = ProductDTOMapper
                .pagedListToDtoResponse(customer.getFavourites(), pageRequest);

        if (productResponse.content.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productResponse);
    }

    @PostMapping("addtocart")
    public ResponseEntity<String> addToCart(@RequestBody ProductForCartRequest request) {
        Product product = productService.findByPCode(request.customerProductPairRequest.productCode);

        if (product == null)
            return ResponseEntity.notFound().build();

        Customer customer = customerService.findById(request.customerProductPairRequest.customerId);

        if (customer == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found!");

        return customerCartService.addToCart(customer, product, request.quantity);
    }

    @DeleteMapping("removefav")
    public ResponseEntity<String> removeFromFavourites(@RequestBody CustomerProductPairRequest pairRequest) {

        Customer customer = customerService.findById(pairRequest.customerId);

        if (customer == null)
            return ResponseEntity.notFound().build();

        Product product = productService.findByPCode(pairRequest.productCode);

        if (product == null)
            return ResponseEntity.notFound().build();

        return customerService.removeFromFavourites(customer, product);
    }

    @GetMapping("cart")
    public ResponseEntity<CustomerCartResponse> showCart(@RequestParam long id) {//TODO sloji broika i ob6ta cena v otdelen dto class
        Customer customer = customerService.findById(id);

        if (customer == null)
            return ResponseEntity.notFound().build();

        List<CustomerCart> customerCarts = customerCartService.cartsByCustomer(customer);

        if (customerCarts.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(CustomerCartResponseBuilder.build(customerCarts));
    }

    @GetMapping("purchase_history")                     //TODO do
    public ResponseEntity<List<CompactPurchaseResponse>> showPurchases(@RequestParam long id) {

        Customer customer = customerService.findById(id);

        if (customer == null)
            return ResponseEntity.notFound().build();

        List<Purchase> purchases = purchaseService.getByCustomer(customer);

        List<CompactPurchaseResponse> responses = new ArrayList<>();

        for (Purchase purchase : purchases) {
            List<PurchaseCart> purchaseCarts = purchaseCartService.getByPurchase(purchase);

            if (purchaseCarts.isEmpty())
                continue;

            List<CompactProductQuantityPair> pairs = new ArrayList<>();

            for (PurchaseCart cart : purchaseCarts) {
                CompactProductQuantityPair pair = new CompactProductQuantityPair();
                pair.compactProductResponse = ProductDTOMapper
                        .entityToCompactResponse(cart.getPurchaseCartId().getProduct());
                pair.quantity = cart.getQuantity();

                pairs.add(pair);
            }

            CompactPurchaseResponse compactPurchaseResponse = CompactPurchaseResponseBuilder.build(purchase, pairs);

            responses.add(compactPurchaseResponse);
        }
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responses);
    }


}
