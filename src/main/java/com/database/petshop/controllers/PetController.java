package com.database.petshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.entity.PetEntity;
import com.database.petshop.service.PetService;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "http://localhost:4200")
public class PetController {

    @Autowired
    private PetService petService;

    @PostMapping("/add")
    public PetEntity addPet(@RequestBody PetEntity pet) {
        return petService.savePet(pet);
    }

    @GetMapping("/customer/{customerId}")
    public List<PetEntity> getPetsByCustomer(@PathVariable Long customerId) {
        return petService.getPetsByCustomerId(customerId);
    }

    @DeleteMapping("/delete/{id}")
    public String deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return "ลบข้อมูลสัตว์เลี้ยง ID: " + id + " เรียบร้อยแล้ว";
    }
}