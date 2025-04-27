package org.hopto.fjavierjp.petregistry.service;

import java.util.Map;
import java.util.Optional;

import org.hopto.fjavierjp.petregistry.dto.PetCreateDTO;
import org.hopto.fjavierjp.petregistry.dto.PetDTO;
import org.hopto.fjavierjp.petregistry.dto.PetFilterDTO;
import org.hopto.fjavierjp.petregistry.exception.ResourceNotFoundException;
import org.hopto.fjavierjp.petregistry.factory.StorageServiceFactory;
import org.hopto.fjavierjp.petregistry.mapper.PetCreateMapper;
import org.hopto.fjavierjp.petregistry.mapper.PetFilterMapper;
import org.hopto.fjavierjp.petregistry.mapper.PetMapper;
import org.hopto.fjavierjp.petregistry.model.Owner;
import org.hopto.fjavierjp.petregistry.model.Pet;
import org.hopto.fjavierjp.petregistry.repository.OwnerRepository;
import org.hopto.fjavierjp.petregistry.repository.PetRepository;
import org.hopto.fjavierjp.petregistry.request.QueryParameters;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
            return Optional.of(PetMapper.toDTO(pet));
        }
        return Optional.empty();
    }

    @Override
    public PetDTO[] find(Optional<QueryParameters> optQueryParameters)
    {
        if (optQueryParameters.isEmpty())
        {
            return this.repository.findAll()
                .stream()
                .map(pet -> PetMapper.toDTO(pet))
                .toArray(PetDTO[]::new);
        }
        Page<Pet> pets = null;
        QueryParameters queryParameters = optQueryParameters.get();
        Pageable pageable = queryParameters.getPageable();
        Map<String, String> filters = queryParameters.getFilters();
        if (filters == null)
        {
            pets = this.repository.findAll(pageable);
        }
        else
        {
            PetFilterDTO petFilterDto = PetFilterMapper.fromMap(filters);
            pets = this.repository.findAll(petFilterDto.getAllSpecifications(), pageable);
        }

        return pets.stream()
            .map(pet -> PetMapper.toDTO(pet))
            .toArray(PetDTO[]::new);
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

        String imageRelativePath = this.storageService.store(petCreateDto.getPicture());
        Pet pet = PetCreateMapper.toEntity(petCreateDto, owner, imageRelativePath);

        try
        {
            Pet savedPet = this.repository.save(pet);
            return PetMapper.toDTO(savedPet);
        }
        catch (Exception exception)
        {
            this.storageService.delete(imageRelativePath);
            throw exception;
        }
    }

    @Override
    public void delete(long id)
    {
        this.repository.deleteById(id);
    }
}
