package com.example.ecomerseapplication.Services;

import com.example.ecomerseapplication.DTOs.CustomerAccountRequest;
import com.example.ecomerseapplication.Entities.Customer;
import com.example.ecomerseapplication.Entities.Product;
import com.example.ecomerseapplication.EntityToDTOConverters.CustomerMapper;
import com.example.ecomerseapplication.Repositories.CustomerRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {


    @Autowired
    CustomerRepository customerRepository;

    public boolean customerExists(String email) {

        return customerRepository.existsByEmail(email);
    }

    public HttpStatus registration(CustomerAccountRequest customerAccountRequest) {

        if (customerExists(customerAccountRequest.email))
            return HttpStatus.CONFLICT;

        Customer customer = CustomerMapper.requestToEntity(customerAccountRequest);

        customer.setCustomerPfp("deffault_pfp.jpg");

        customerRepository.save(customer);

        return HttpStatus.CREATED;
    }

    public ResponseEntity<Integer> logIn(CustomerAccountRequest customerAccountRequest) {

        if (!customerExists(customerAccountRequest.email))
            return ResponseEntity.notFound().build();

        if (BCrypt.checkpw(customerAccountRequest.password, customerRepository
                .getPassword(customerAccountRequest.email)
                .replaceAll(",", "")))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerRepository.getIdByEmail(customerAccountRequest.email));

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<HttpStatus> addProductToFavourites(long customerId, Product product) {

        Customer customer = customerRepository.findById(customerId).orElse(null);


        if (customer ==null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        customer.getFavourites().add(product);
        customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public Customer findById(long id) {
        return customerRepository.findById(id).orElse(null);
    }


    public ResponseEntity<String> removeFromFavourites(Customer customer, Product product) {

        customer.getFavourites().remove(product);

        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.OK).body("Product successfully removed!");
    }

}
