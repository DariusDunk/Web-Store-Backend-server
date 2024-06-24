package com.example.ecomerseapplication.EntityToDTOConverters;

import com.example.ecomerseapplication.DTOs.CompactProductQuantityPair;
import com.example.ecomerseapplication.DTOs.CompactPurchaseResponse;
import com.example.ecomerseapplication.Entities.Purchase;
import java.util.List;

public class CompactPurchaseResponseBuilder {

    public static CompactPurchaseResponse build(Purchase purchase, List<CompactProductQuantityPair> pairs) {
        CompactPurchaseResponse response = new CompactPurchaseResponse();

        response.purchaseCode = purchase.getPurchaseCode();
        response.purchaseDate = purchase.getDate();
        response.totalCost = purchase.getTotalCost();
        response.compactProductQuantityPairs = pairs;

        return response;
    }
}
