package org.hopto.fjavierjp.petregistry.unit.mapper;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.hopto.fjavierjp.petregistry.dto.PetCreateDTO;
import org.hopto.fjavierjp.petregistry.mapper.PetCreateMapper;
import org.hopto.fjavierjp.petregistry.model.Owner;
import org.hopto.fjavierjp.petregistry.model.Pet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.datafaker.Faker;

@ExtendWith(SpringExtension.class)
public class PetCreateMapperTest
{
    @TestConfiguration
    public static class PetCreateMapperTestConfiguration
    {
        @Bean
        public Faker faker()
        {
            return new Faker();
        }
    }

    @Autowired
    private Faker faker;

    @Test
    public void testToEntityFullDTO()
    {
        String petCreateDtoName = this.faker.name().toString();
        Date petCreateDtoBirthDate = this.faker.date().past(365, TimeUnit.DAYS);
        Pet.Species petCreateDtoSpecies = Pet.Species.getRandomSpecies();
        float petCreateDtoWeight = (float) this.faker.number().randomDouble(2, 1, 100);
        boolean petCreateDtoChipped = this.faker.bool().bool();
        MockMultipartFile petCreateDtoPicture = new MockMultipartFile(
            this.faker.name().toString(),
            this.faker.file().fileName().toString(),
            this.faker.file().mimeType().toString(),
            this.faker.lordOfTheRings().toString().getBytes()
        );
        long petCreateDtoOwnerId = Long.parseLong(this.faker.numerify("#".repeat(13)));
        String ownerName = this.faker.name().toString();
        
        PetCreateDTO petCreateDto = new PetCreateDTO(
            petCreateDtoName,
            petCreateDtoBirthDate,
            petCreateDtoSpecies,
            petCreateDtoWeight,
            petCreateDtoChipped,
            petCreateDtoPicture,
            petCreateDtoOwnerId
        );

        Owner owner = new Owner(
            ownerName,
            this.faker.address().toString(),
            this.faker.phoneNumber().toString()
        );

        String newFilename = this.faker.file().fileName();
        Pet pet = PetCreateMapper.toEntity(petCreateDto, owner, newFilename);
        
        assert pet.getName().equals(petCreateDtoName);
        assert pet.getBirthDate().equals(petCreateDtoBirthDate);
        assert pet.getSpecies().equals(petCreateDtoSpecies);
        assert pet.getWeight() == petCreateDtoWeight;
        assert pet.getChipped() == petCreateDtoChipped;
        assert pet.getPictureUrl().equals(newFilename);
        assert pet.getOwner().getName() == ownerName;
    }

    @Test
    public void testToEntityRequiredDTO()
    {
        String petCreateDtoName = this.faker.name().toString();
        Pet.Species petCreateDtoSpecies = Pet.Species.getRandomSpecies();
        MockMultipartFile petCreateDtoPicture = new MockMultipartFile(
            this.faker.name().toString(),
            this.faker.file().fileName().toString(),
            this.faker.file().mimeType().toString(),
            this.faker.lordOfTheRings().toString().getBytes()
        );
        String ownerName = this.faker.name().toString();

        PetCreateDTO petCreateDTO = new PetCreateDTO(
            petCreateDtoName,
            petCreateDtoSpecies,
            petCreateDtoPicture
        );

        Owner owner = new Owner(
            ownerName,
            this.faker.address().toString(),
            this.faker.phoneNumber().toString()
        );

        String newFilename = this.faker.file().fileName();
        Pet pet = PetCreateMapper.toEntity(petCreateDTO, owner, newFilename);

        assert pet.getName().equals(petCreateDtoName);
        assert pet.getSpecies().equals(petCreateDtoSpecies);
        assert pet.getPictureUrl().equals(newFilename);
        assert pet.getOwner().getName().equals(ownerName);
    }
}
