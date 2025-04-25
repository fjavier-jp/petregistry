package org.hopto.fjavierjp.petregistry.mapper;

import java.util.Date;

import org.hopto.fjavierjp.petregistry.dto.PetDTO;
import org.hopto.fjavierjp.petregistry.model.Owner;
import org.hopto.fjavierjp.petregistry.model.Pet;

public class PetMapper
{
    public static PetDTO toDTO(Pet pet, String imagePublicUrl)
    {
        PetDTO petDto = new PetDTO();
        petDto.setName(pet.getName());
        petDto.setSpecies(pet.getSpecies().toString());
        petDto.setPictureUrl(imagePublicUrl);

        Date birthDate = pet.getBirthDate();
        if (birthDate != null)
        {
            petDto.setBirthDate(birthDate.toString());
        }

        Boolean chipped = pet.getChipped();
        if (chipped != null)
        {
            petDto.setChipped(chipped);
        }

        Float weight = pet.getWeight();
        if (weight != null)
        {
            petDto.setWeight(weight);
        }

        Owner owner = pet.getOwner();
        String ownerName = "";
        if (owner != null)
        {
            ownerName = owner.getName();
        }
        petDto.setOwnerName(ownerName);
        return petDto;
    }
}
