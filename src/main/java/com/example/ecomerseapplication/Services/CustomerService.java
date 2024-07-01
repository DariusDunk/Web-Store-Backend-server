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

    public ResponseEntity<String> registration(CustomerAccountRequest customerAccountRequest) {

        if (customerExists(customerAccountRequest.email))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Този имейл вече съществува!");

        if (incorrectPassword(customerAccountRequest.password.toCharArray()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Паролата трябва да е поне 8 символа дълга и да има поне един символ от следните: главни букви, малки букви и цифри!");

        Customer customer = CustomerMapper.requestToEntity(customerAccountRequest);

        customer.setCustomerPfp("deffault_pfp.jpg");

        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body("Регистрацията е успешна!");
    }

    public ResponseEntity<Long> logIn(CustomerAccountRequest customerAccountRequest) {

        if (!customerExists(customerAccountRequest.email))
            return ResponseEntity.notFound().build();

        if (BCrypt.checkpw(customerAccountRequest.password, String.valueOf(customerRepository
                .getPassword(customerAccountRequest.email))))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerRepository.getIdByEmail(customerAccountRequest.email));

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<String > addProductToFavourites(long customerId, Product product) {

        Customer customer = customerRepository.findById(customerId).orElse(null);

        if (customer == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        customer.getFavourites().add(product);
        customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Продуктът е добавен в любими!");
    }

    public Customer findById(long id) {
        return customerRepository.findById(id).orElse(null);
    }


    public ResponseEntity<String> removeFromFavourites(Customer customer, Product product) {

        customer.getFavourites().remove(product);

        customerRepository.save(customer);

        return ResponseEntity.status(HttpStatus.OK).body("Продуктът е успешно премахнат!");
    }

    public Customer getByEmail(String email) {
        return customerRepository.getByEmail(email).orElse(null);
    }

    public boolean incorrectPassword(char[] password) {
        if (password == null || password.length < 8) {
            return true;
        }

        boolean noUppercase = true;
        boolean noLowercase = true;
        boolean noDigit = true;

        for (char c : password) {
            if (Character.isUpperCase(c)) {
                noUppercase = false;
            } else if (Character.isLowerCase(c)) {
                noLowercase = false;
            } else if (Character.isDigit(c)) {
                noDigit = false;
            }
        }
        return noDigit || noLowercase || noUppercase;
    }

    public ResponseEntity<String> passwordUpdate(Customer customer, String password) {

        if (BCrypt.checkpw(password, String.valueOf(customer.getPassword())))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Новата парола не може да е същата като старата!");

        if (incorrectPassword(password.toCharArray()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Паролата трябва да е поне 8 символа дълга и да има поне един символ от следните: главни букви, малки букви и цифри!");

        customer.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(10)).toCharArray());

        customerRepository.save(customer);

        return ResponseEntity.ok().body("Паролата бе убновена успешно!");
    }
}
