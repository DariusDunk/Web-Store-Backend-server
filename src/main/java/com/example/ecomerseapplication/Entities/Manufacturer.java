package com.example.ecomerseapplication.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "manufacturers", schema = "online_shop")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "products")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id")
    private int id;

    @Column(name = "manufacturer_name", columnDefinition = "character varying(30)")
    private String manufacturerName;

    public Manufacturer(int id, String manufacturerName) {
        this.id = id;
        this.manufacturerName = manufacturerName;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "manufacturer")
    private List<Product> products;
}
