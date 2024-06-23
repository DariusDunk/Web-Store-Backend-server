package com.example.ecomerseapplication.Entities;

import com.example.ecomerseapplication.DTOs.SavedPurchaseDetailsDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saved_purchase_details", schema = "online_shop")
@Data
@NoArgsConstructor
public class SavedPurchaseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private long id;

    @Column(name = "contact_name", columnDefinition = "character varying(50)")
    private String contactName;

    @Column(name = "contact_number", columnDefinition = "character varying(10)")
    private String contactNumber;

    private String address;

    @JoinColumn(name = "customer_id")
    @OneToOne
    private Customer customer;

    public SavedPurchaseDetails(SavedPurchaseDetailsDto savedPurchaseDetailsDto, Customer customer) {
        this.contactName = savedPurchaseDetailsDto.contactName;
        this.contactNumber = savedPurchaseDetailsDto.contactNumber;
        this.address = savedPurchaseDetailsDto.address;
        this.customer = customer;
    }
}
