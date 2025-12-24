package com.database.petshop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.database.petshop.entity.StatusEntity;
import com.database.petshop.service.StatusService;

@RestController
@RequestMapping("/api/status")
@CrossOrigin(origins = "http://localhost:4200")
public class StatusController {
    @Autowired private StatusService statusService;

    @GetMapping("/all")
    public ResponseEntity<List<StatusEntity>> getAllStatuses() {
        return ResponseEntity.ok(statusService.findAllStatuses());
    }
}
