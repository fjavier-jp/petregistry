package org.hopto.fjavierjp.petregistry.mapper;

import java.util.ArrayList;
import java.util.List;

import org.hopto.fjavierjp.petregistry.dto.OwnerDTO;
import org.hopto.fjavierjp.petregistry.dto.PetDTO;
import org.hopto.fjavierjp.petregistry.model.Owner;
import org.hopto.fjavierjp.petregistry.model.Pet;

public class OwnerMapper
{
    public static OwnerDTO toDTO(Owner owner)
    {
        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet pet : owner.getPets())
        {
            petDTOs.add(PetMapper.toDTO(pet));
        }
        
        return new OwnerDTO(
            owner.getName(),
            owner.getAddress(),
            owner.getPhone(),
            petDTOs
        );
    }
}
