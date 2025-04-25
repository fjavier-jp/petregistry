package org.hopto.fjavierjp.petregistry.dto;

import lombok.Data;

@Data
public class PetDTO
{
    private String name;
    private String birthDate;
    private String species;
    private float weight;
    private boolean chipped;
    private String pictureUrl;
    private String ownerName;
}
