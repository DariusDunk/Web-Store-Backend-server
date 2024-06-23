package com.example.ecomerseapplication;
import com.example.ecomerseapplication.Entities.CategoryAttribute;
import com.example.ecomerseapplication.Entities.CustomerCart;
import com.example.ecomerseapplication.Entities.ProductCategory;
import com.example.ecomerseapplication.Others.PurchaseCodeGenerator;
import com.example.ecomerseapplication.Services.CategoryAttributeService;
import com.example.ecomerseapplication.Services.CustomerCartService;
import com.example.ecomerseapplication.Services.CustomerService;
import com.example.ecomerseapplication.Services.ProductCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class EComerseApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    ProductCategoryService categoryService;

    @Autowired
    CategoryAttributeService categoryAttributeService;

    @Autowired
    CustomerCartService customerCartService;

    @Autowired
    CustomerService customerService;

    @Value("jasypt.encryptor.password")
    String key;

    @Test
    void attributeTest() {
        Optional<ProductCategory> productCategory = categoryService.findById(4);

        List<CategoryAttribute> categoryAttributeList = categoryAttributeService.getByCategory(productCategory.orElse(null));

        System.out.println(categoryAttributeList.get(0).getAttributeOption());
    }

    @Test
    void encryptTest() {

        char[] pass = {'l','u','d','o','t','Z','a','v','e','t'};

        String pwHash = BCrypt.hashpw(Arrays.toString(pass),BCrypt.gensalt(10));

        String candidate = Arrays.toString(pass);

        System.out.println(BCrypt.checkpw(candidate, pwHash));
    }

    @Test
    void codeHashTest() {
        System.out.println(PurchaseCodeGenerator.generateCode(LocalDateTime.now()));
    }

    @Test
    void cartsByCustomer() {


        List<CustomerCart> customerCarts = customerCartService.cartsByCustomer(customerService.findById(6));

        if (customerCarts.isEmpty())
            System.out.println("Error");
        else
            System.out.println("Finished");
    }
}
