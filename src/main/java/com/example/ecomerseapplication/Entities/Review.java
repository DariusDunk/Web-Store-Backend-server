package com.example.ecomerseapplication.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "reviews", schema = "online_shop")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long id;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    @JoinColumn(name = "customer_id")
    @ManyToOne
    private Customer customer;

    @Column(name = "review_text", columnDefinition = "character varying(500)")
    private String reviewText;

    private short rating;
}
