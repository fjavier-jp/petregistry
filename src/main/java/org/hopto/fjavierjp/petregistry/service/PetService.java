package org.hopto.fjavierjp.petregistry.service;

import java.util.Optional;

import org.hopto.fjavierjp.petregistry.dto.PetCreateDTO;
import org.hopto.fjavierjp.petregistry.dto.PetDTO;

public interface PetService
{
    public Optional<PetDTO> findById(long id);
    public PetDTO store(PetCreateDTO petCreateDto);
    public StorageService getStorageService();
}
