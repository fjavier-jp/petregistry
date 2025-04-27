package org.hopto.fjavierjp.petregistry.mapper;

import java.util.Map;

import org.hopto.fjavierjp.petregistry.dto.PetFilterDTO;
public class PetFilterMapper
{
    public static PetFilterDTO fromMap(Map<String, String> filters)
    {
        PetFilterDTO petFilterDto = new PetFilterDTO();
        if (filters.containsKey("species"))
        {
            petFilterDto.setSpecies(filters.get("species"));
        }
        if (filters.containsKey("ownerId"))
        {
            petFilterDto.setOwnerId(Long.parseLong(filters.get("ownerId")));
        }
        return petFilterDto;
    }
}
