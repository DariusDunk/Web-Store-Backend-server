package com.example.ecomerseapplication.EntityToDTOConverters;

import com.example.ecomerseapplication.DTOs.ManufacturerDTO;
import java.util.HashSet;
import java.util.Set;

public class ManufacturerConverter {

    public static Set<ManufacturerDTO> objectArrSetToDtoSet(Set<Object[]> objects) {

        if (objects.isEmpty())
            return null;

        Set<ManufacturerDTO> manufacturerDTOSet = new HashSet<>();

        for (Object[] objectsArr : objects) {

            ManufacturerDTO manufacturerDTO = new ManufacturerDTO();
            manufacturerDTO.id = Integer.parseInt((objectsArr[0]).toString());
            manufacturerDTO.name = objectsArr[1].toString();

            manufacturerDTOSet.add(manufacturerDTO);
        }

        return manufacturerDTOSet;
    }
}
