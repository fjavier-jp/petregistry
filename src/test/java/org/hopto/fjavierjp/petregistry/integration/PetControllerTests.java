package org.hopto.fjavierjp.petregistry.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import net.datafaker.Faker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.hopto.fjavierjp.petregistry.dto.PetCreateDTO;
import org.hopto.fjavierjp.petregistry.dto.PetDTO;
import org.hopto.fjavierjp.petregistry.model.Pet;
import org.hopto.fjavierjp.petregistry.repository.PetRepository;
import org.hopto.fjavierjp.petregistry.service.PetService;
import org.hopto.fjavierjp.petregistry.util.UrlGenerator;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PetControllerTests
{
    @Autowired
    private MockMvc mockMvc;
    private Faker faker = new Faker();
    @Autowired
    private PetRepository repository;
    @Autowired
    private PetService service;

    @BeforeEach
    public void setUp()
    {
        this.service.getStorageService().deleteAll();
        this.service.getStorageService().reinit();
    }

    @Test
    public void testShow() throws Exception
    {
        String fakeName = this.faker.name().toString();
        Pet.Species fakeSpecies = Pet.Species.getRandomSpecies();
        String fakeFilename = this.faker.file().fileName();
        Pet pet = new Pet.Builder()
            .withName(fakeName)
            .withSpecies(fakeSpecies)
            .withPictureUrl(fakeFilename)
            .build();
        this.repository.save(pet);

        this.mockMvc.perform(get("/pets/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(fakeName))
            .andExpect(jsonPath("$.species").value(fakeSpecies.toString()))
            .andExpect(jsonPath("$.pictureUrl").value(UrlGenerator.generatePublicUrl(fakeFilename)));
    }

    @Test
    public void testStoreUnsupportedMediaType() throws Exception
    {
        this.mockMvc.perform(post("/pets"))
            .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testStoreBadRequestOnlyFile() throws Exception
    {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "HOla".getBytes());
        PetCreateDTO petCreateDto = new PetCreateDTO.Builder().build();
        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(
                multipart("/pets")
                    .file(mockFile)
                    .content(objectMapper.writeValueAsString(petCreateDto))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testStoreBadRequestNoName() throws Exception
    {
        MockMultipartFile mockFile = new MockMultipartFile("picture", "test.txt", "text/plain", "HOla".getBytes());

        this.mockMvc.perform(
                multipart("/pets")
                    .file(mockFile)
                    .param("species", Pet.Species.getRandomSpecies().toString())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testStoreBadRequestNoSpecies() throws Exception
    {
        MockMultipartFile mockFile = new MockMultipartFile("picture", "test.txt", "text/plain", "HOla".getBytes());

        this.mockMvc.perform(
                multipart("/pets")
                    .file(mockFile)
                    .param("name", this.faker.name().toString())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testStoreBadRequestNoFile() throws Exception
    {
        this.mockMvc.perform(
                multipart("/pets")
                    .param("name", this.faker.name().toString())
                    .param("species", Pet.Species.getRandomSpecies().toString())
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testStoreBadRequestInvalidBirthDate() throws Exception
    {
        MockMultipartFile mockFile = new MockMultipartFile("picture", "test.txt", "text/plain", "HOla".getBytes());

        this.mockMvc.perform(
                multipart("/pets")
                    .file(mockFile)
                    .param("name", this.faker.name().toString())
                    .param("species", Pet.Species.getRandomSpecies().toString())
                    .param("birthDate", this.faker.date().future(2, 1, TimeUnit.DAYS, "yyyy-MM-dd"))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testStoreBadRequestInvalidWeight() throws Exception
    {
        MockMultipartFile mockFile = new MockMultipartFile("picture", "test.txt", "text/plain", "HOla".getBytes());

        this.mockMvc.perform(
                multipart("/pets")
                    .file(mockFile)
                    .param("name", this.faker.name().toString())
                    .param("species", Pet.Species.getRandomSpecies().toString())
                    .param("weight", Integer.toString(this.faker.number().negative()))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testStoreBadRequestInvalidOwner() throws Exception
    {
        MockMultipartFile mockFile = new MockMultipartFile("picture", "test.txt", "text/plain", "HOla".getBytes());

        this.mockMvc.perform(
                multipart("/pets")
                    .file(mockFile)
                    .param("name", this.faker.name().toString())
                    .param("species", Pet.Species.getRandomSpecies().toString())
                    .param("ownerId", Long.toString(this.faker.random().nextLong()))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isNotFound());
    }

    @Test
    public void testStoreOkAndGetPicture() throws Exception
    {
        String fakeName = this.faker.name().toString();
        String fakeSpecies = Pet.Species.getRandomSpecies().toString();
        String fakeFilename = this.faker.file().fileName();
        String fakeContent = this.faker.lordOfTheRings().toString();
        MockMultipartFile mockFile = new MockMultipartFile("picture", fakeFilename, null, fakeContent.getBytes());

        MvcResult mvcResult = this.mockMvc.perform(
                multipart("/pets")
                    .file(mockFile)
                    .param("name", fakeName)
                    .param("species", fakeSpecies)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
            )
            .andExpect(status().isCreated())
            .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        PetDTO petDto = objectMapper.readValue(responseContent, PetDTO.class);
        String petPictureUrl = petDto.getPictureUrl();

        assertNotNull(petDto);
        assertEquals(fakeName, petDto.getName());
        assertEquals(fakeSpecies, petDto.getSpecies());
        assertNotNull(petPictureUrl);
        
        this.mockMvc.perform(get(petPictureUrl))
            .andExpect(status().isOk())
            .andExpect(content().string(fakeContent));

        // Cleaning the test.
        String[] urlParts = petPictureUrl.split("/");
        String filename = urlParts[urlParts.length - 1];
        Path absolutePath = this.service.getStorageService().getAbsolutePath();
        this.service.getStorageService().delete(absolutePath.resolve(filename).toString());
    }
}
