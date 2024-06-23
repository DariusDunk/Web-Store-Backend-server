package com.example.ecomerseapplication.Entities;

import com.example.ecomerseapplication.CompositeIdClasses.CustomerCartId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer_carts",schema = "online_shop")
@Getter
@Setter
@NoArgsConstructor
public class CustomerCart {

    @EmbeddedId
    private CustomerCartId customerCartId;

    private short quantity;

    public CustomerCart(CustomerCartId customerCartId, short quantity) {
        this.customerCartId = customerCartId;
        this.quantity = quantity;
    }

}
