package com.example.ecomerseapplication.EntityToDTOConverters;

import com.example.ecomerseapplication.DTOs.CustomerAccountRequest;
import com.example.ecomerseapplication.Entities.Customer;
import org.mindrot.jbcrypt.BCrypt;

public class CustomerMapper {

    private String key;
    public static Customer requestToEntity(CustomerAccountRequest customerAccountRequest) {

        Customer customer = new Customer();
        customer.setName(customerAccountRequest.userName);
        customer.setEmail(customerAccountRequest.email);
        customer.setPassword(
                BCrypt.hashpw(customerAccountRequest.password,
                        BCrypt.gensalt(10)).toCharArray()
        );

        return customer;
    }
}
