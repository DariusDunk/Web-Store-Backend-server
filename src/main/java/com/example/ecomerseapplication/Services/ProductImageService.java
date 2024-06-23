package com.example.ecomerseapplication.Services;

import com.example.ecomerseapplication.Repositories.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImageService {

    @Autowired
    ProductImageRepository productImageRepository;
}
