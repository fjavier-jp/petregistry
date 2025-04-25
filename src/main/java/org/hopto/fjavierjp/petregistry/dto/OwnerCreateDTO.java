package org.hopto.fjavierjp.petregistry.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnerCreateDTO
{
    private String name;
    private String address;
    private String phone;
}
