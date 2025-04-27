package org.hopto.fjavierjp.petregistry.service;

import org.hopto.fjavierjp.petregistry.dto.OwnerDTO;
import org.hopto.fjavierjp.petregistry.exception.ResourceNotFoundException;
import org.hopto.fjavierjp.petregistry.mapper.OwnerMapper;
import org.hopto.fjavierjp.petregistry.model.Pet;
import org.hopto.fjavierjp.petregistry.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService
{
    @Autowired
    private PetRepository petRepository;

    @Override
    public OwnerDTO findByPetId(long petId)
    {
        Pet pet = this.petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("Pet does not exist"));
        return OwnerMapper.toDTO(pet.getOwner());
    }

}
