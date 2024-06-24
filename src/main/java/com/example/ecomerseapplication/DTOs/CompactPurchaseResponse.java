package com.example.ecomerseapplication.DTOs;

import java.time.LocalDateTime;
import java.util.List;

public class CompactPurchaseResponse {
    public String purchaseCode;
    public LocalDateTime purchaseDate;
    public int totalCost;

    public List<CompactProductQuantityPair> compactProductQuantityPairs;


}
