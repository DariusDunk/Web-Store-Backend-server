package com.example.ecomerseapplication.EntityToDTOConverters;

import com.example.ecomerseapplication.DTOs.PurchaseDto;
import com.example.ecomerseapplication.DTOs.PurchaseResponse;
import com.example.ecomerseapplication.Entities.Purchase;
import com.example.ecomerseapplication.Others.PurchaseCodeGenerator;

public class PurchaseMapper {

    public static Purchase requestToEntity(PurchaseDto purchaseDto) {
        Purchase purchase = new Purchase();
        purchase.setPurchaseCode(PurchaseCodeGenerator.generateCode(purchase.getDate()));
        purchase.setAddress(purchaseDto.address);
        purchase.setContactName(purchaseDto.contactName);
        purchase.setContactNumber(purchaseDto.contactNumber);

        return purchase;
    }
    public static PurchaseResponse entityToResponse(Purchase purchase) {
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        purchaseResponse.purchaseCode = purchase.getPurchaseCode();
        purchaseResponse.totalCost = purchase.getTotalCost();
        purchaseResponse.dateOfPurchase = purchase.getDate();

        PurchaseDto purchaseDto = new PurchaseDto();
        purchaseDto.address = purchase.getAddress();
        purchaseDto.contactName= purchase.getContactName();
        purchaseDto.contactNumber = purchase.getContactNumber();

        purchaseResponse.purchaseDto = purchaseDto;

        return purchaseResponse;
    }
}
