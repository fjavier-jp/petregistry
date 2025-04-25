package org.hopto.fjavierjp.petregistry.dto;

import java.util.Date;

import org.hopto.fjavierjp.petregistry.model.Pet.Species;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class PetCreateDTO
{
    @Size(min = 1, max = 255, message = "Name length must be between 1 and 255 characters")
    @NotBlank(message = "Name is mandatory")
    @NonNull
    private String name;

    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @NotNull(message = "Species is mandatory")
    @Enumerated(EnumType.STRING)
    @NonNull
    private Species species;

    @DecimalMin(value = "0.0", inclusive = true, message = "Weight must be positive")
    private Float weight;

    private Boolean chipped;
    
    @NotNull(message = "Picture is mandatory")
    @NonNull
    private MultipartFile picture;

    private Long ownerId;

    private PetCreateDTO() {}

    public static class Builder
    {
        private PetCreateDTO petCreateDto;

        public Builder()
        {
            this.petCreateDto = new PetCreateDTO();
        }

        public Builder withName(String name)
        {
            this.petCreateDto.setName(name);
            return this;
        }
        public Builder withBirthDate(Date birthDate)
        {
            this.petCreateDto.setBirthDate(birthDate);
            return this;
        }
        public Builder withSpecies(Species species)
        {
            this.petCreateDto.setSpecies(species);
            return this;
        }
        public Builder withWeight(float weight)
        {
            this.petCreateDto.setWeight(weight);
            return this;
        }
        public Builder withChipped(boolean chipped)
        {
            this.petCreateDto.setChipped(chipped);
            return this;
        }
        public Builder withPicture(MultipartFile picture)
        {
            this.petCreateDto.setPicture(picture);
            return this;
        }
        public Builder withOwnerId(long ownerId)
        {
            this.petCreateDto.setOwnerId(ownerId);
            return this;
        }
        public PetCreateDTO build()
        {
            return this.petCreateDto;
        }
    }
}
