package com.example.ecomerseapplication.DTOs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PurchaseResponse {
    public PurchaseDto purchaseDto;
    public int totalCost;
    public LocalDateTime dateOfPurchase;
    public String purchaseCode;
    public List<CompactProductQuantityPair> productQuantityPairs = new ArrayList<>();

}
