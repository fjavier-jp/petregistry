package org.hopto.fjavierjp.petregistry.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hopto.fjavierjp.petregistry.dto.PetDTO;
import org.hopto.fjavierjp.petregistry.mapper.PetMapper;
import org.hopto.fjavierjp.petregistry.model.Pet;
import org.hopto.fjavierjp.petregistry.util.UrlGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.datafaker.Faker;

@ExtendWith(SpringExtension.class)
public class PetMapperTest
{
    private String fakePictureUrl;

    @TestConfiguration
    public static class PetMapperTestConfiguration
    {
        @Bean
        public Faker faker()
        {
            return new Faker();
        }
    }
    
    @Autowired
    private Faker faker;

    @BeforeEach
    public void setUp()
    {
        fakePictureUrl = this.faker.file().fileName();
    }

    @Test
    public void testToDTOSimplePet()
    {
        String fakeName = this.faker.name().toString();
        Pet.Species fakeSpecies = Pet.Species.getRandomSpecies();
        String url = this.faker.file().fileName();
        Pet pet = new Pet.Builder()
            .withId(Long.parseLong(this.faker.numerify("#".repeat(13))))
            .withName(fakeName)
            .withSpecies(fakeSpecies)
            .withPictureUrl(url)
            .build();

        try (MockedStatic<UrlGenerator> mockedUrlGenerator = Mockito.mockStatic(UrlGenerator.class))
        {
            mockedUrlGenerator.when(() -> UrlGenerator.generatePublicUrl(url)).thenReturn(fakePictureUrl);
            PetDTO petDTO = PetMapper.toDTO(pet);
    
            assertNotNull(petDTO);
            assertEquals(fakeName, petDTO.getName());
            assertEquals(fakeSpecies.toString(), petDTO.getSpecies());
            assertEquals(fakePictureUrl, petDTO.getPictureUrl());
        }
    }
}
