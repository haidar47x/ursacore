package com.ursacore.service;

import com.ursacore.model.PatientDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {

    List<PatientDTO> listPatients();

    Optional<PatientDTO> getPatientById(UUID patientId);

    PatientDTO createNewPatient(PatientDTO patientDTO);

    void updatePatientById(UUID patientId, PatientDTO patientDTO);

    void deleteById(UUID patientId);

    void patchPatientById(UUID patientId, PatientDTO patientDTO);
}
