package org.hopto.fjavierjp.petregistry.repository;

import org.hopto.fjavierjp.petregistry.model.Owner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends CrudRepository<Owner, Long> {}
