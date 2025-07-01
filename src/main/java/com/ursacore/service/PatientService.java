package com.ursacore.service;

import com.ursacore.model.Patient;

import java.util.List;
import java.util.UUID;

public interface PatientService {

    List<Patient> listPatients();

    Patient getPatientById(UUID patientId);

    Patient createNewPatient(Patient patient);

    void updatePatientById(UUID patientId, Patient patient);

    void deleteById(UUID patientId);

    void patchPatientById(UUID patientId, Patient patient);
}
