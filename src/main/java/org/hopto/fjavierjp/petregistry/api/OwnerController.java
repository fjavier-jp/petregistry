package org.hopto.fjavierjp.petregistry.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.hopto.fjavierjp.petregistry.dto.OwnerDTO;
import org.hopto.fjavierjp.petregistry.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/owners")
public class OwnerController
{
    @Autowired
    private OwnerService service;

    @GetMapping("/pets/{petId}")
    public ResponseEntity<OwnerDTO> showOwnerByPet(@PathVariable long petId)
    {
        return ResponseEntity.ok(this.service.findByPetId(petId));
    }
}
