package com.ursacore.controller;

import com.ursacore.exceptions.NotFoundException;
import com.ursacore.model.Patient;
import com.ursacore.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@Slf4j
public class PatientController {

    public static final String PATIENT_PATH = "/api/v1/patient";
    public static final String PATIENT_PATH_ID = PATIENT_PATH + "/{patientId}";

    private final PatientService patientService;

    @GetMapping(PATIENT_PATH)
    public List<Patient> listPatients() {
        return patientService.listPatients();
    }

    @GetMapping(PATIENT_PATH_ID)
    public Patient getPatientById(@PathVariable("patientId") UUID patientId) {
        return patientService.getPatientById(patientId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(PATIENT_PATH)
    public ResponseEntity createNewPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.createNewPatient(patient);
        HttpHeaders headers = new HttpHeaders();
        var id = savedPatient.getId();
        headers.add("Location", "/api/v1/patient/" + savedPatient.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(PATIENT_PATH_ID)
    public ResponseEntity updatePatientById(@PathVariable("patientId") UUID patientId, @RequestBody Patient patient) {
        patientService.updatePatientById(patientId, patient);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(PATIENT_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("patientId") UUID patientId) {
        patientService.deleteById(patientId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(PATIENT_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("patientId") UUID patientId, @RequestBody Patient patient) {
        patientService.patchPatientById(patientId, patient);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
