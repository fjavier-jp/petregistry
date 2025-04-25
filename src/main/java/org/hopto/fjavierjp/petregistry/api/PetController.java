package org.hopto.fjavierjp.petregistry.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.hopto.fjavierjp.petregistry.dto.PetCreateDTO;
import org.hopto.fjavierjp.petregistry.dto.PetDTO;
import org.hopto.fjavierjp.petregistry.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@RestController
@RequestMapping("/pets")
public class PetController
{
    @Autowired
    PetService service;

    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> show(@PathVariable long id)
    {
        Optional<PetDTO> opt = this.service.findById(id);
        if (opt.isPresent())
        {
            return ResponseEntity.ok(opt.get());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PetDTO> store(@Valid @ModelAttribute PetCreateDTO createDto)
    {
        return new ResponseEntity<PetDTO>(this.service.store(createDto), HttpStatus.CREATED);
    }
}
