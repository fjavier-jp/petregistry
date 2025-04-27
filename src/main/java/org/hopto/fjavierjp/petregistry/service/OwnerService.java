package org.hopto.fjavierjp.petregistry.service;

import org.hopto.fjavierjp.petregistry.dto.OwnerDTO;

public interface OwnerService
{
    public OwnerDTO findByPetId(long petId);
}
