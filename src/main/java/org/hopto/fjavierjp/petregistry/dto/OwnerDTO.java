package org.hopto.fjavierjp.petregistry.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnerDTO
{
    private String name;
    private String address;
    private String phone;
    private List<PetDTO> pets;
}
