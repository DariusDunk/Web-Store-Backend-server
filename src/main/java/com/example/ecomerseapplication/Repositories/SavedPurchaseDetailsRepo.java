package com.example.ecomerseapplication.Repositories;

import com.example.ecomerseapplication.Entities.Customer;
import com.example.ecomerseapplication.Entities.SavedPurchaseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SavedPurchaseDetailsRepo extends JpaRepository<SavedPurchaseDetails, Long> {
    Optional<SavedPurchaseDetails> getByCustomer(Customer customer);
}
