package com.example.ecomerseapplication.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Table(name = "category_attributes", schema = "online_shop")
@Data
@EqualsAndHashCode(exclude = "products")
public class CategoryAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_id")
    private int id;


    @JoinColumn(name = "product_category_id")
    @ManyToOne(cascade = {CascadeType.ALL})
    private ProductCategory productCategory;

    @JoinColumn(name = "attribute_name_id")
    @ManyToOne
    private AttributeName attributeName;

    @Column(name = "attribute_option", columnDefinition = "character varying(50)")
    private String attributeOption;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.ALL},mappedBy = "categoryAttributeSet")
    private List<Product> products;
}
