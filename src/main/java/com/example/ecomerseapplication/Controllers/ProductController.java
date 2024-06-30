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
    CustomerService customerService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ProductCategoryService productCategoryService;

    @Autowired
    ManufacturerService manufacturerService;

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

    @GetMapping("{productCode}")
    public ResponseEntity<DetailedProductResponse> detailedProductInfo(@PathVariable String productCode,
                                                                       @RequestParam long id) {

        Customer customer = customerService.findById(id);
        ResponseEntity<DetailedProductResponse> detailedProductResponse = productService
                .getByNameAndCode(productCode, customer);

        return Objects.requireNonNullElseGet(detailedProductResponse, () -> ResponseEntity.notFound().build());
    }

    @GetMapping("manufacturer/{manufacturerName}/p{page}")
    public ResponseEntity<Page<CompactProductResponse>> productsByManufacturer(@PathVariable String manufacturerName,
                                                                               @PathVariable int page) {
        PageRequest pageRequest = PageRequest.of(page, PageContentLimit.limit);
        Manufacturer manufacturer = manufacturerService.findByName(manufacturerName);

        if (manufacturer == null) {
            return ResponseEntity.notFound().build();
        }

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

    @GetMapping("category/{name}/p{page}")
    public ResponseEntity<Page<CompactProductResponse>> getProductsByCategory(@PathVariable String name,
                                                                              @PathVariable int page) {

        if (name.equals("Бензинови машини") || name.equals("електрически машини"))
            return ResponseEntity.notFound().build();

        PageRequest pageRequest = PageRequest.of(page, PageContentLimit.limit);

        ProductCategory productCategory = productCategoryService.findByName(name);
        if (productCategory == null) {
            return ResponseEntity.notFound().build();
        }

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
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Само стойности от 1-5 са позволени!");

        Customer customer = customerService.findById(request.customerId);

        if (customer == null)
            return ResponseEntity.notFound().build();

        Product product = productService.findByPCode(request.productCode);

        if (product == null)
            return ResponseEntity.notFound().build();

        Product updatedProduct = reviewService.manageReview(product, customer, request);

        if (updatedProduct == null)
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("Не беше извършена промяна");

        productService.save(updatedProduct);

        return ResponseEntity.ok().body("Ревюто е качено!");
    }

    @DeleteMapping("deletereview")
    public ResponseEntity<String> deleteReview(@RequestBody CustomerProductPairRequest pairRequest) {
        Customer customer = customerService.findById(pairRequest.customerId);

        if (customer == null)
            return ResponseEntity.notFound().build();

        Product product = productService.findByPCode(pairRequest.productCode);

        if (product == null)
            return ResponseEntity.notFound().build();

        Review review = reviewService.getByProdCust(product, customer);

        if (review == null)
            return ResponseEntity.notFound().build();

        short newRating = reviewService.updatedRating(product, review);

        if (newRating == -1)
            return ResponseEntity.notFound().build();

        product.setRating(newRating);
        product.getReviews().remove(review);
        productService.save(product);
        reviewService.delete(review);

        return ResponseEntity.ok().body("Ревюто е изтрито");
    }
}
