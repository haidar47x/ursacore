package com.ursacore.service;

import com.ursacore.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Primary
@Service
public class PatientServiceImpl implements PatientService {

    private final Map<UUID, Patient> patientMap;

    public PatientServiceImpl() {
        Patient p1 = Patient.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .version(1)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        Patient p2 = Patient.builder()
                .id(UUID.randomUUID())
                .name("Jan Doe")
                .version(1)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        Patient p3 = Patient.builder()
                .id(UUID.randomUUID())
                .name("Jonas Doe")
                .version(1)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        this.patientMap = new HashMap<>();
        patientMap.put(p1.getId(), p1);
        patientMap.put(p2.getId(), p2);
        patientMap.put(p3.getId(), p3);
    }

    @Override
    public List<Patient> listPatients() {
        return new ArrayList<>(patientMap.values());
    }

    @Override
    public Optional<Patient> getPatientById(UUID patientId) {
        return Optional.of(patientMap.get(patientId));
    }

    @Override
    public Patient createNewPatient(Patient patient) {
        Patient savedPatient = Patient.builder()
                .id(UUID.randomUUID())
                .version(1)
                .name(patient.getName())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        patientMap.put(savedPatient.getId(), savedPatient);
        return savedPatient;
    }

    @Override
    public void updatePatientById(UUID patientId, Patient patient) {
        Patient existing = patientMap.get(patientId);
        existing.setName(patient.getName());
        existing.setUpdateAt(LocalDateTime.now());
    }

    @Override
    public void deleteById(UUID patientId) {
        patientMap.remove(patientId);
    }

    @Override
    public void patchPatientById(UUID patientId, Patient patient) {
        Patient existing = patientMap.get(patientId);

        if (patient.getName() != null) {
            existing.setName(patient.getName());
        }
    }
}
