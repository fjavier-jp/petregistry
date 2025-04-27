package org.hopto.fjavierjp.petregistry.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.hopto.fjavierjp.petregistry.service.PetServiceImpl;
import org.hopto.fjavierjp.petregistry.service.StorageService;
import org.hopto.fjavierjp.petregistry.util.UrlGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.datafaker.Faker;

@ExtendWith(SpringExtension.class)
public class PetServiceImplTests
{
    @Mock
    private PetRepository repository;
    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private StorageService storageService;
    @Mock
    private StorageServiceFactory storageServiceFactory;
    private PetServiceImpl service;
    private String storageServiceDirectory;

    @TestConfiguration
    public static class PetServiceImplTestsContextConfiguration
    {
        @Bean
        public Faker faker()
        {
            return new Faker();
        }
        @Bean
        public Environment environment()
        {
            MockEnvironment mockEnvironment = new MockEnvironment();
            mockEnvironment.setProperty("storage.images.location", "target/test-uploads/images/");
            mockEnvironment.setProperty("server.address", "http://localhost:8080");
            return mockEnvironment;
        }
    }

    @Autowired
    private Faker faker;
    @Autowired
    private Environment environment;

    @BeforeEach
    public void setUp()
    {
        this.storageServiceDirectory = this.environment.getProperty("storage.images.location") + "pets/";
        Mockito.when(this.storageServiceFactory.create(this.storageServiceDirectory)).thenReturn(this.storageService);
        this.service = new PetServiceImpl(this.repository, this.ownerRepository, this.environment, this.storageServiceFactory);
    }

    @Test
    public void testFindByIdNotFound()
    {
        long id = Long.parseLong(this.faker.numerify("#".repeat(13)));

        Mockito.when(this.repository.findById(id)).thenReturn(Optional.empty());
        Optional<PetDTO> opt = this.service.findById(id);

        assertFalse(opt.isPresent());
    }

    @Test
    public void testFindById()
    {
        long id = Long.parseLong(this.faker.numerify("#".repeat(13)));
        String fileUrl = this.faker.file().fileName();
        Pet pet = new Pet.Builder()
            .withId(id)
            .withName(this.faker.name().toString())
            .withSpecies(Pet.Species.getRandomSpecies())
            .withPictureUrl(fileUrl)
            .withWeight((float) this.faker.number().randomDouble(2, 1, 100))
            .build();

        Mockito.when(this.repository.findById(id)).thenReturn(Optional.of(pet));
        try (MockedStatic<UrlGenerator> mockedUrlGenerator = Mockito.mockStatic(UrlGenerator.class))
        {
            mockedUrlGenerator.when(() -> UrlGenerator.generatePublicUrl(fileUrl)).thenReturn(fileUrl);
            Optional<PetDTO> opt = this.service.findById(id);

            assertTrue(opt.isPresent());
            PetDTO petDto = opt.get();
            assertTrue(petDto.getName() == pet.getName());
            assertTrue(petDto.getSpecies() == pet.getSpecies().toString());
            assertTrue(petDto.getWeight() == pet.getWeight());
        }
    }

    @Test
    public void testStoreOwnerNotFound()
    {
        long ownerId = Long.parseLong(this.faker.numerify("#".repeat(13)));
        Mockito.when(this.ownerRepository.findById(ownerId)).thenReturn(Optional.empty());
        MockMultipartFile mockFile = new MockMultipartFile(
            this.faker.name().toString(),
            this.faker.file().fileName().toString(),
            this.faker.file().mimeType().toString(),
            this.faker.lordOfTheRings().toString().getBytes()
        );
        PetCreateDTO petCreateDto = new PetCreateDTO.Builder()
            .withName(this.faker.name().toString())
            .withSpecies(Pet.Species.getRandomSpecies())
            .withPicture(mockFile)
            .withOwnerId(ownerId)
            .build();
        
        assertThrows(ResourceNotFoundException.class, () -> this.service.store(petCreateDto));
    }

    @Test
    public void testStoreStorageServiceFails()
    {
        String originalFilename = this.faker.file().fileName().toString();
        String filename = this.faker.file().fileName();
        MockMultipartFile mockFile = new MockMultipartFile(
            originalFilename,
            this.faker.file().fileName(),
            this.faker.file().mimeType(),
            this.faker.lordOfTheRings().toString().getBytes()
        );
        Mockito.when(this.storageService.store(mockFile)).thenReturn(filename);
        Mockito.when(this.storageServiceFactory.create(this.environment.getProperty("storage.images.location", "uploads/images/") + "pets/")).thenReturn(this.storageService);

        MockitoException mockException = new MockitoException("");
        Mockito.when(this.storageService.store(mockFile)).thenThrow(mockException);

        String petName = this.faker.name().toString();
        Pet.Species petSpecies = Pet.Species.getRandomSpecies();
        PetCreateDTO petCreateDto = new PetCreateDTO.Builder()
            .withName(petName)
            .withSpecies(petSpecies)
            .withPicture(mockFile)
            .build();

        assertThrows(Exception.class, () -> this.service.store(petCreateDto));
    }

    @Test
    public void testStore()
    {
        long ownerId = Long.parseLong(this.faker.numerify("#".repeat(13)));
        String ownerName = this.faker.name().toString();
        Owner owner = new Owner(
            ownerName,
            this.faker.address().toString(),
            this.faker.phoneNumber().toString()
        );
        Mockito.when(this.ownerRepository.findById(ownerId)).thenReturn(Optional.of(owner));

        String filename = this.faker.file().fileName();
        MockMultipartFile mockFile = new MockMultipartFile(
            this.faker.file().fileName().toString(),
            this.faker.file().fileName(),
            this.faker.file().mimeType(),
            this.faker.lordOfTheRings().toString().getBytes()
        );
        Mockito.when(this.storageService.store(mockFile)).thenReturn(filename);
        Mockito.when(this.storageServiceFactory.create(this.storageServiceDirectory)).thenReturn(this.storageService);

        String petName = this.faker.name().toString();
        Pet.Species petSpecies = Pet.Species.getRandomSpecies();
        Pet pet = new Pet.Builder()
            .withName(petName)
            .withSpecies(petSpecies)
            .withPictureUrl(filename)
            .withOwner(owner)
            .build();
        PetCreateDTO petCreateDto = new PetCreateDTO.Builder()
            .withName(petName)
            .withSpecies(petSpecies)
            .withPicture(mockFile)
            .withOwnerId(ownerId)
            .build();

        String publicPictureUrl = this.faker.file().fileName();
        PetDTO fakePetDto = new PetDTO();
        fakePetDto.setName(petName);
        fakePetDto.setSpecies(petSpecies.toString());
        fakePetDto.setPictureUrl(publicPictureUrl);
        fakePetDto.setOwnerName(ownerName);

        try (MockedStatic<PetCreateMapper> mockedPetCreateMapper = Mockito.mockStatic(PetCreateMapper.class))
        {
            mockedPetCreateMapper.when(() -> PetCreateMapper.toEntity(petCreateDto, owner, filename)).thenReturn(pet);
            Mockito.when(this.repository.save(pet)).thenReturn(pet);

            try (MockedStatic<PetMapper> mockedPetMapper = Mockito.mockStatic(PetMapper.class))
            {
                mockedPetMapper.when(() -> PetMapper.toDTO(pet)).thenReturn(fakePetDto);
                PetDTO petDto = this.service.store(petCreateDto);
        
                assertEquals(petDto.getName(), petName);
                assertEquals(petDto.getSpecies(), petSpecies.toString());
                assertEquals(petDto.getPictureUrl(), publicPictureUrl);
                assertEquals(petDto.getOwnerName(), ownerName);
            }
        }
    }
}
