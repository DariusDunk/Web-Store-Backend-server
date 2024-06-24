package com.example.ecomerseapplication.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "customers", schema = "online_shop")
@Data
@EqualsAndHashCode(exclude = {"reviews","savedPurchaseDetails", "purchases"})
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private long id;

    @Column(columnDefinition = "character varying(50)")
    private String name;
    @Column(columnDefinition = "character varying(50)")
    private String email;

    private char[] password;
    @Column(name = "phone_number", columnDefinition = "character varying(10)")
    private String phoneNumber;

    @CreationTimestamp
    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "customer_pfp", columnDefinition = "character varying(255) default 'default_pfp.jpg'")
    private String customerPfp;

    @OneToMany(mappedBy = "customer")
    private List<Purchase> purchases;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "customer")
    private SavedPurchaseDetails savedPurchaseDetails;

    @JoinTable(name = "favourites", schema = "online_shop",
    joinColumns = @JoinColumn(name="customer_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
    @ManyToMany(cascade = CascadeType.REMOVE)
    List<Product> favourites;

    @OneToMany(mappedBy = "customer")
    private List<Review> reviews;

}
