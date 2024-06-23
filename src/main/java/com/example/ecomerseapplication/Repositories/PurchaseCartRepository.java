package com.example.ecomerseapplication.Repositories;

import com.example.ecomerseapplication.CompositeIdClasses.PurchaseCartId;
import com.example.ecomerseapplication.Entities.Product;
import com.example.ecomerseapplication.Entities.Purchase;
import com.example.ecomerseapplication.Entities.PurchaseCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseCartRepository extends JpaRepository<PurchaseCart, PurchaseCartId> {

    @Query(value = "select pc.purchaseCartId.product " +
            "from PurchaseCart pc " +
            "where pc.purchaseCartId.purchase = ?1")
    List<Product> productsOfCustomer(Purchase purchase);


}
