package com.ursacore.controller;

import com.ursacore.exception.NotFoundException;
import com.ursacore.model.BloodType;
import com.ursacore.model.PatientDTO;
import com.ursacore.service.PatientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@Slf4j
public class PatientController {

    public static final String PATIENT_PATH = "/api/v1/patient";
    public static final String PATIENT_PATH_ID = PATIENT_PATH + "/{patientId}";

    private final PatientService patientService;

    @GetMapping(PATIENT_PATH)
    public Page<PatientDTO> listPatients(@RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "bloodType", required = false) BloodType bloodType,
                                         @RequestParam(value = "page", required = false) Integer pageNumber,
                                         @RequestParam(value = "numResults", required = false) Integer pageSize)  {
        return patientService.listPatients(name, bloodType, pageNumber, pageSize);
    }

    @GetMapping(PATIENT_PATH_ID)
    public PatientDTO getPatientById(@PathVariable("patientId") UUID patientId) {
        return patientService.getPatientById(patientId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(PATIENT_PATH)
    public ResponseEntity<Void> createPatient(@Validated @RequestBody PatientDTO patientDTO) {
        PatientDTO savedPatientDTO = patientService.saveNewPatient(patientDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/patient/" + savedPatientDTO.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(PATIENT_PATH_ID)
    public ResponseEntity<Void> updatePatientById(@PathVariable("patientId") UUID patientId, @Validated @RequestBody PatientDTO patientDTO) {
        patientService.updatePatientById(patientId, patientDTO)
                .orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(PATIENT_PATH_ID)
    public ResponseEntity<Void> deletePatientById(@PathVariable("patientId") UUID patientId) {
        if (!patientService.deletePatientById(patientId))
            throw new NotFoundException();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(PATIENT_PATH_ID)
    public ResponseEntity<Void> patchPatientById(@PathVariable("patientId") UUID patientId, @RequestBody PatientDTO patientDTO) {
        patientService.patchPatientById(patientId, patientDTO).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
