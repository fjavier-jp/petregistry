package org.hopto.fjavierjp.petregistry.service;

import java.util.Optional;

import org.hopto.fjavierjp.petregistry.dto.PetCreateDTO;
import org.hopto.fjavierjp.petregistry.dto.PetDTO;
import org.hopto.fjavierjp.petregistry.request.QueryParameters;

public interface PetService
{
    public Optional<PetDTO> findById(long id);
    public PetDTO[] find(Optional<QueryParameters> queryParameters);
    public PetDTO store(PetCreateDTO petCreateDto);
    public StorageService getStorageService();
    public void delete(long id);
}
