package com.example.ecomerseapplication.Repositories;

import com.example.ecomerseapplication.Entities.CategoryAttribute;
import com.example.ecomerseapplication.Entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, Integer> {
    List<CategoryAttribute> findByProductCategory(ProductCategory productCategory);

    @Query(value = "select ca.attributeName.id " +
            "from CategoryAttribute ca " +
            "where ca.productCategory = :category ")
    List<Integer> findNameIdsByCategory(@Param("category") ProductCategory productCategory);

    @Query(value = "select ca " +
            "from CategoryAttribute ca " +
            "where ca.attributeName.id in :nameIds and ca.attributeOption in :options")
    Set<CategoryAttribute> findByNameIdAndOption(@Param("nameIds") Set<Integer> nameIds, @Param("options") Set<String> options);
}
