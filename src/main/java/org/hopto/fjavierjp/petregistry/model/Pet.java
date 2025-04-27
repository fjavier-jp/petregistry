package org.hopto.fjavierjp.petregistry.model;

import java.util.Date;
import java.util.Random;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@AllArgsConstructor
@Table(name = "pets")
public class Pet
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private Date birthDate;

    @Enumerated(EnumType.STRING)
    private Species species;
    
    private Float weight;
    private Boolean chipped;
    private String pictureUrl;
    
    @ManyToOne
    private Owner owner;
    
    private Pet() {}

    public enum Species
    {
        CAT,
        DOG,
        BIRD,
        REPTILE,
        FISH,
        OTHER;

        public static Species getRandomSpecies()
        {
            Random random = new Random();
            Species[] species = Species.values();
            return species[random.nextInt(species.length)];
        }
    }

    public static class Builder
    {
        private Pet pet;

        public Builder()
        {
            pet = new Pet();
        }

        public Builder withId(long id)
        {
            pet.id = id;
            return this;
        }

        public Builder withName(String name)
        {
            pet.name = name;
            return this;
        }

        public Builder withBirthDate(Date birthDate)
        {
            pet.birthDate = birthDate;
            return this;
        }

        public Builder withSpecies(Species species)
        {
            pet.species = species;
            return this;
        }

        public Builder withWeight(float weight)
        {
            pet.weight = weight;
            return this;
        }

        public Builder withChipped(boolean chipped)
        {
            pet.chipped = chipped;
            return this;
        }

        public Builder withPictureUrl(String pictureUrl)
        {
            pet.pictureUrl = pictureUrl;
            return this;
        }

        public Builder withOwner(Owner owner)
        {
            pet.owner = owner;
            return this;
        }

        public Pet build()
        {
            return pet;
        }
    }
}
