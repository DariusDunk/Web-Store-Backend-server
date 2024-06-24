package com.example.ecomerseapplication.DTOs;


import java.util.ArrayList;
import java.util.List;

public class CustomerCartResponse {
    public List<CompactProductQuantityPair> productQuantityPair = new ArrayList<>();
    public int totalCost;
}
