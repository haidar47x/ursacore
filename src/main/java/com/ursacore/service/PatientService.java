package com.ursacore.service;

import com.ursacore.model.PatientDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {

    List<PatientDTO> listPatients(String name);

    Optional<PatientDTO> getPatientById(UUID patientId);

    PatientDTO saveNewPatient(PatientDTO patientDTO);

    Optional<PatientDTO> updatePatientById(UUID patientId, PatientDTO patientDTO);

    Boolean deletePatientById(UUID patientId);

    Optional<PatientDTO> patchPatientById(UUID patientId, PatientDTO patientDTO);
}
