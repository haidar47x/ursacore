package com.ursacore.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PatientController {

    @GetMapping
    public List<String> getPatients() {
        return List.of("Patient 1", "Patient 2", "Patient 3");
    }

}
