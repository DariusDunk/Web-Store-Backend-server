package com.example.ecomerseapplication.Services;

import com.example.ecomerseapplication.Entities.ProductCategory;
import com.example.ecomerseapplication.Repositories.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryService {

    @Autowired
    ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }

    public List<String> getAllCategoryNames() {
        return productCategoryRepository.getAllNames();
    }

    public Optional<ProductCategory> findById(int id) {
        return productCategoryRepository.findById(id);
    }

    public ProductCategory findByName(String name) {
        return productCategoryRepository.findByCategoryName(name).orElse(null);
    }
}
