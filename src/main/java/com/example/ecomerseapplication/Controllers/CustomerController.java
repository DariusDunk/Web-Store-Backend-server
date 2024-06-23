package com.example.ecomerseapplication.Controllers;

import com.example.ecomerseapplication.DTOs.*;
import com.example.ecomerseapplication.Entities.Customer;
import com.example.ecomerseapplication.Entities.Product;
import com.example.ecomerseapplication.EntityToDTOConverters.ProductDTOMapper;
import com.example.ecomerseapplication.Others.PageContentLimit;
import com.example.ecomerseapplication.Services.CustomerCartService;
import com.example.ecomerseapplication.Services.CustomerService;
import com.example.ecomerseapplication.Services.ProductService;
import com.example.ecomerseapplication.Services.SavedPurchaseDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    @Autowired
    CustomerCartService customerCartService;

    @Autowired
    SavedPurchaseDetailsService purchaseDetailsService;

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
    public ResponseEntity<List<CompactProductResponse>> showCart(@RequestParam long id) {
        Customer customer = customerService.findById(id);

        if (customer == null)
            return ResponseEntity.notFound().build();

        List<Product> products = customerCartService.productsOfCustomerCart(customer);

        List<CompactProductResponse> compactProductResponses = products
                .stream()
                .map(ProductDTOMapper::entityToCompactResponse)
                .toList();
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(compactProductResponses);
    }
}
