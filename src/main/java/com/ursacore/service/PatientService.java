package com.ursacore.service;

import com.ursacore.model.BloodType;
import com.ursacore.model.PatientDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface PatientService {

    Page<PatientDTO> listPatients(String name, BloodType bloodType, Integer pageNumber, Integer pageSize);

    Optional<PatientDTO> getPatientById(UUID patientId);

    PatientDTO saveNewPatient(PatientDTO patientDTO);

    Optional<PatientDTO> updatePatientById(UUID patientId, PatientDTO patientDTO);

    Boolean deletePatientById(UUID patientId);

    Optional<PatientDTO> patchPatientById(UUID patientId, PatientDTO patientDTO);
}
