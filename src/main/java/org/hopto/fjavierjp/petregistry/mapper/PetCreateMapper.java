package org.hopto.fjavierjp.petregistry.mapper;

import java.util.Date;

import org.hopto.fjavierjp.petregistry.dto.PetCreateDTO;
import org.hopto.fjavierjp.petregistry.model.Owner;
import org.hopto.fjavierjp.petregistry.model.Pet;

public class PetCreateMapper
{
    public static Pet toEntity(PetCreateDTO createDto, Owner owner, String imageFilename)
    {
        Pet.Builder builder = new Pet.Builder()
           .withName(createDto.getName())
           .withSpecies(createDto.getSpecies())
           .withPictureUrl(imageFilename)
           .withOwner(owner);
        
        Date birthDate = createDto.getBirthDate();
        if (birthDate != null)
        {
            builder.withBirthDate(birthDate);
        }

        Boolean chipped = createDto.getChipped();
        if (chipped != null)
        {
            builder.withChipped(chipped);
        }

        Float weight = createDto.getWeight();
        if (weight != null)
        {
            builder.withWeight(weight);
        }

        return builder.build();
    }
}
