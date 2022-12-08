package com.example.devopsproject.controllers;

import com.example.devopsproject.models.Owner;
import com.example.devopsproject.services.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner")
@CrossOrigin(origins = "*")
public class OwnerController {
    @Autowired
    OwnerService ownerService;

    @GetMapping("/all")
    public List<Owner> getOwners()
    {
        List<Owner> ownerslist = this.ownerService.getAllOwners();
        return ownerslist;
    }


    @GetMapping("/{ownerId}")
    public Owner getOwnerByID(@PathVariable("ownerId") Long ownerID) throws Exception
    {
        Owner owner = this.ownerService.getOwnerById(ownerID);
        return owner;
    }

    @PostMapping("/create")
    public Owner createNewOwner(@RequestBody Owner newOwner)
    {
        Owner owner = this.ownerService.createOwner(newOwner);
        return owner;
    }

}