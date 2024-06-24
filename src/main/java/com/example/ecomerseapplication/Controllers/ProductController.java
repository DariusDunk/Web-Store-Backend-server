package com.example.ecomerseapplication.Controllers;


import com.example.ecomerseapplication.DTOs.*;
import com.example.ecomerseapplication.Entities.*;
import com.example.ecomerseapplication.Others.PageContentLimit;
import com.example.ecomerseapplication.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("product/")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryAttributeService categoryAttributeService;

    @Autowired
    CustomerCartService customerCartService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ReviewService reviewService;

    @GetMapping("findall")
    public Page<Product> findAll(@RequestParam int page) {
        PageRequest pageRequest = PageRequest.of(page, PageContentLimit.limit);
        return productService.findAllProductsPage(pageRequest);
    }

    @GetMapping("search")
    public ResponseEntity<Page<CompactProductResponse>> findByNameLike(@RequestParam String name, @RequestParam int page) {
        PageRequest pageRequest = PageRequest.of(page, PageContentLimit.limit);

        Page<CompactProductResponse> responsePages = productService.getProductsLikeName(pageRequest, name);

        if (responsePages.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(responsePages);
    }

    @GetMapping("suggest")
    public List<String> getNameSuggestions(@RequestParam String name) {
        return productService.getNameSuggestions(name);
    }

    @GetMapping("{name}/{productCode}")
    public ResponseEntity<DetailedProductResponse> detailedProductInfo(@PathVariable String name,
                                                                       @PathVariable String productCode,
                                                                       @RequestParam long id) {

        Customer customer = customerService.findById(id);
        ResponseEntity<DetailedProductResponse> detailedProductResponse = productService.getByNameAndCode(name,
                productCode, customer);


        return Objects.requireNonNullElseGet(detailedProductResponse, () -> ResponseEntity.notFound().build());

    }

    @GetMapping("manufacturer/{manufacturerName}-{id}/p{page}")
    public ResponseEntity<Page<CompactProductResponse>> productsByManufacturer(@PathVariable String manufacturerName,
                                                                               @PathVariable int id,
                                                                               @PathVariable int page) {
        PageRequest pageRequest = PageRequest.of(page, PageContentLimit.limit);
        Manufacturer manufacturer = new Manufacturer(id, manufacturerName);

        Page<CompactProductResponse> productResponsePage = productService.getByManufacturer(manufacturer, pageRequest);

        if (productResponsePage.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productResponsePage);
    }

    @GetMapping("featured")
    public ResponseEntity<List<CompactProductResponse>> featuredProducts() {

        List<CompactProductResponse> compactProductResponses = productService.getFeaturedProducts();

        if (compactProductResponses.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(compactProductResponses);

    }

    @GetMapping("category/{name}-{id}/p{page}")
    public ResponseEntity<Page<CompactProductResponse>> getProductsByCategory(@PathVariable String name,
                                                                              @PathVariable int id,
                                                                              @PathVariable int page) {

        if (id == 1 || id == 6)
            return ResponseEntity.notFound().build();

        PageRequest pageRequest = PageRequest.of(page, PageContentLimit.limit);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);
        productCategory.setCategoryName(name);

        Page<CompactProductResponse> productResponsePage = productService.getByCategory(productCategory, pageRequest);

        if (productResponsePage.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(productResponsePage);

    }

    @PostMapping("filter/{page}")
    public CompactProductPagedListDto productByFilterAndManufacturer(@RequestBody ProductFilterRequest productFilterRequest,
                                                                     @PathVariable int page) {
        Set<CategoryAttribute> categoryAttributeSet = null;

        if (productFilterRequest.filterAttributes != null) {
            categoryAttributeSet = categoryAttributeService.getByNameAndOption(
                    productFilterRequest.filterAttributes.keySet(),
                    new HashSet<>(productFilterRequest.filterAttributes.values()));
        }

        PageRequest pageRequest = PageRequest.of(page, 10);

        return productService.getByCategoryFiltersManufacturerAndPriceRange(categoryAttributeSet,
                productFilterRequest.productCategory,
                productFilterRequest.priceLowest,
                productFilterRequest.priceHighest,
                productFilterRequest.manufacturer,
                pageRequest);
    }

    @PostMapping("review")
    public ResponseEntity<String> addReview(@RequestBody ReviewRequest request) {

        if (request.rating > 5 || request.rating < 1)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Only rating values of 1-5 are allowed");

        short adjustedRating = (short) (request.rating * 10);
        Customer customer = customerService.findById(request.customerId);

        if (customer == null)
            return ResponseEntity.notFound().build();

        Product product = productService.findByPCode(request.productCode);

        if (product == null)
            return ResponseEntity.notFound().build();

        Product updatedProduct = reviewService.manageReview(product, customer, request);

        if (updatedProduct == null)
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("No change was made!");

        productService.save(updatedProduct);

        return ResponseEntity.ok().body("Review posted");
    }
}
