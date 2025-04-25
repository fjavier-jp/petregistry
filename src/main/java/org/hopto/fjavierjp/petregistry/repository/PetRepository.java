package org.hopto.fjavierjp.petregistry.repository;

import org.hopto.fjavierjp.petregistry.model.Pet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends CrudRepository<Pet, Long> {}
