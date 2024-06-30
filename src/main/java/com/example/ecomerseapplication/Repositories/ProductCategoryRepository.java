package com.example.ecomerseapplication.Repositories;

import com.example.ecomerseapplication.Entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    Optional<ProductCategory> findByCategoryName(String name);

    @Query(value = "select c.categoryName " +
            "from ProductCategory c " +
            "where c.categoryName!= 'електрически машини' AND c.categoryName!= 'Бензинови машини'")
    List<String> getAllNames();
}
