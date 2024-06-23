package com.example.ecomerseapplication.Controllers;

import com.example.ecomerseapplication.Entities.CategoryAttribute;
import com.example.ecomerseapplication.Entities.ProductCategory;
import com.example.ecomerseapplication.Services.CategoryAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryAttributeController {

    @Autowired
    CategoryAttributeService categoryAttributeService;

    @GetMapping("getattributes")
    public List<CategoryAttribute> findAll() {
        return categoryAttributeService.getAll();
    }

    @GetMapping("attributebycategory")
    public List<CategoryAttribute> findByCategory(ProductCategory productCategory) {
        return categoryAttributeService.getByCategory(productCategory);
    }
}
