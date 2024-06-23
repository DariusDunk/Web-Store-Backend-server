package com.example.ecomerseapplication.DTOs;


import java.util.HashSet;
import java.util.Set;

public class CategoryAttributesResponse {
    public int nameId;
    public String attributeName;
    public Set<String> options= new HashSet<>();
}
