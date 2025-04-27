package org.hopto.fjavierjp.petregistry.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name = "owners")
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Owner
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 4, max = 255, message = "Name length must be between 4 and 255 characters")
    @NotBlank(message = "Name is mandatory")
    @NonNull
    private String name;

    @Size(min = 10, max = 511, message = "Address length must be between 10 and 511 characters")
    @NotBlank(message = "Address is mandatory")
    @NonNull
    private String address;

    @NotBlank(message = "Phone is mandatory")
    @NonNull
    private String phone;

    @OneToMany(mappedBy = "owner")
    private List<Pet> pets;

    public void addPet(Pet pet)
    {
        pets.add(pet);
    }
}
