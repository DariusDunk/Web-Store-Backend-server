package com.example.ecomerseapplication.Repositories;

import com.example.ecomerseapplication.Entities.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Long> {


}
