package com.example.ecomerseapplication.Services;

import com.example.ecomerseapplication.Entities.AttributeName;
import com.example.ecomerseapplication.Entities.ProductCategory;
import com.example.ecomerseapplication.Repositories.AttributeNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class AttributeNameService {

    @Autowired
    AttributeNameRepository attributeNameRepository;

    public Set<AttributeName> getNameSetByCategory(ProductCategory productCategory) {
        return attributeNameRepository.getByCategory(productCategory);
    }
}
