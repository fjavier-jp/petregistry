package org.hopto.fjavierjp.petregistry.dto;

import org.hopto.fjavierjp.petregistry.model.Pet;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;

@Data
public class PetFilterDTO
{
    private String species;
    private Long ownerId;

    public Specification<Pet> equalsSpecies()
    {
        return new Specification<Pet>()
        {
            @Override
            public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
            {
                return criteriaBuilder.equal(root.get("species"), species);
            }   
        };
    }

    public Specification<Pet> equalsOwnerId()
    {
        return new Specification<Pet>()
        {
            @Override
            public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
            {
                return criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
            }   
        };
    }

    public Specification<Pet> getAllSpecifications()
    {
        return new Specification<Pet>()
        {
            @Override
            public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder)
            {
                Predicate predicate = criteriaBuilder.conjunction();

                if (species != null)
                {
                    predicate = criteriaBuilder.and(predicate, equalsSpecies().toPredicate(root, criteriaQuery, criteriaBuilder));
                }

                if (ownerId != null)
                {
                    predicate = criteriaBuilder.and(predicate, equalsOwnerId().toPredicate(root, criteriaQuery, criteriaBuilder));
                }

                return predicate;
            }
        };
    }
}
