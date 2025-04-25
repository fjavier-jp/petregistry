package org.hopto.fjavierjp.petregistry.service;

import java.util.Optional;

import org.hopto.fjavierjp.petregistry.dto.PetCreateDTO;
import org.hopto.fjavierjp.petregistry.dto.PetDTO;
import org.hopto.fjavierjp.petregistry.exception.ResourceNotFoundException;
import org.hopto.fjavierjp.petregistry.factory.StorageServiceFactory;
import org.hopto.fjavierjp.petregistry.mapper.PetCreateMapper;
import org.hopto.fjavierjp.petregistry.mapper.PetMapper;
import org.hopto.fjavierjp.petregistry.model.Owner;
import org.hopto.fjavierjp.petregistry.model.Pet;
import org.hopto.fjavierjp.petregistry.repository.OwnerRepository;
import org.hopto.fjavierjp.petregistry.repository.PetRepository;
import org.hopto.fjavierjp.petregistry.util.UrlGenerator;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class PetServiceImpl implements PetService
{
    private PetRepository repository;
    private OwnerRepository ownerRepository;
    private Environment environment;
    private StorageServiceFactory storageServiceFactory;
    private StorageService storageService;
    private String imagesLocation;
    
    public PetServiceImpl(PetRepository repository, OwnerRepository ownerRepository, Environment environment, StorageServiceFactory storageServiceFactory)
    {
        this.environment = environment;
        this.ownerRepository = ownerRepository;
        this.storageServiceFactory = storageServiceFactory;
        this.repository = repository;

        this.imagesLocation = this.environment.getProperty("storage.images.location", "uploads/images/") + "pets/";
        this.storageService = this.storageServiceFactory.create(this.imagesLocation);
    }

    @Override
    public StorageService getStorageService()
    {
        return this.storageService;
    }

    @Override
    public Optional<PetDTO> findById(long id)
    {
        Optional<Pet> opt = this.repository.findById(id);
        if (opt.isPresent())
        {
            Pet pet = opt.get();
            return Optional.of(PetMapper.toDTO(pet, this.imagesLocation + pet.getPictureUrl()));
        }
        return Optional.empty();
    }

    @Override
    public PetDTO store(PetCreateDTO petCreateDto)
    {
        Long ownerId = petCreateDto.getOwnerId();
        Owner owner = null;
        if (ownerId != null)
        {
            owner = this.ownerRepository.findById(ownerId).orElseThrow(() -> new ResourceNotFoundException("Owner does not exist"));
        }

        String imageFilename = this.storageService.store(petCreateDto.getPicture());
        Pet pet = PetCreateMapper.toEntity(petCreateDto, owner, imageFilename);
        
        String publicUrl = UrlGenerator.generatePublicUrl(this.imagesLocation, imageFilename);
        try
        {
            Pet savedPet = this.repository.save(pet);
            return PetMapper.toDTO(savedPet, publicUrl);
        }
        catch (Exception exception)
        {
            this.storageService.delete(imageFilename);
            throw exception;
        }
    }
}
