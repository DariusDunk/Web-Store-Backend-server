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
import org.springframework.transaction.annotation.Transactional;

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

    public ResponseEntity<Long> logIn(CustomerAccountRequest customerAccountRequest) {

        if (!customerExists(customerAccountRequest.email))
            return ResponseEntity.notFound().build();

//        if (BCrypt.checkpw(customerAccountRequest.password, customerRepository
//                .getPassword(customerAccountRequest.email)
//                .replaceAll(",", "")))

        if (BCrypt.checkpw(customerAccountRequest.password, String.valueOf(customerRepository
                .getPassword(customerAccountRequest.email))))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(customerRepository.getIdByEmail(customerAccountRequest.email));

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<HttpStatus> addProductToFavourites(long customerId, Product product) {

        Customer customer = customerRepository.findById(customerId).orElse(null);


        if (customer == null)
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

    public Customer getByEmail(String email) {
        return customerRepository.getByEmail(email).orElse(null);
    }

    public boolean passwordValidation(char[] password) {
        if (password == null || password.length < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (char c : password) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }
        return hasDigit && hasLowercase && hasUppercase;
    }

    public ResponseEntity<String> passwordUpdate(Customer customer, String password) {

        if (BCrypt.checkpw(password, String.valueOf(customer.getPassword())))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New password cannot be the same as the old password");

        if (!passwordValidation(password.toCharArray()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password needs to be at least 8 symbols long and have at least one of each: capital letters, lowercase letters, digits");

        customer.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(10)).toCharArray());

        customerRepository.save(customer);

        return ResponseEntity.ok().body("Password successfully updated!");
    }
}
