package com.example.ecomerseapplication.EntityToDTOConverters;

import com.example.ecomerseapplication.DTOs.SavedPurchaseDetailsDto;
import com.example.ecomerseapplication.Entities.SavedPurchaseDetails;

public class SavedPurchaseDetailsMapper {

    public static SavedPurchaseDetailsDto entityToDto(SavedPurchaseDetails inputEntity) {
        SavedPurchaseDetailsDto dto = new SavedPurchaseDetailsDto();

        dto.contactName = inputEntity.getContactName();
        dto.contactNumber = inputEntity.getContactNumber();
        dto.address = inputEntity.getAddress();

        return dto;
    }
}
