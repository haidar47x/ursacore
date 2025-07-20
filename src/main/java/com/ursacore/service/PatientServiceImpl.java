package com.ursacore.service;

import com.ursacore.model.BloodType;
import com.ursacore.model.PatientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final Map<UUID, PatientDTO> patientMap;

    public PatientServiceImpl() {
        PatientDTO p1 = PatientDTO.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .version(1)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        PatientDTO p2 = PatientDTO.builder()
                .id(UUID.randomUUID())
                .name("Jan Doe")
                .version(1)
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        PatientDTO p3 = PatientDTO.builder()
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
    public List<PatientDTO> listPatients(String name, BloodType bloodType) {
        return new ArrayList<>(patientMap.values());
    }

    @Override
    public Optional<PatientDTO> getPatientById(UUID patientId) {
        return Optional.of(patientMap.get(patientId));
    }

    @Override
    public PatientDTO saveNewPatient(PatientDTO patientDTO) {
        PatientDTO savedPatientDTO = PatientDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .name(patientDTO.getName())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        patientMap.put(savedPatientDTO.getId(), savedPatientDTO);
        return savedPatientDTO;
    }

    @Override
    public Optional<PatientDTO> updatePatientById(UUID patientId, PatientDTO patientDTO) {
        PatientDTO existing = patientMap.get(patientId);
        if (existing == null) {
            return Optional.empty();
        }
        existing.setName(patientDTO.getName());
        existing.setUpdateAt(LocalDateTime.now());
        return Optional.of(existing);
    }

    @Override
    public Boolean deletePatientById(UUID patientId) {
        patientMap.remove(patientId);
        return true;
    }

    @Override
    public Optional<PatientDTO> patchPatientById(UUID patientId, PatientDTO patientDTO) {
        PatientDTO existing = patientMap.get(patientId);

        if (existing == null)
            return Optional.empty();

        existing.setName(patientDTO.getName());
        return Optional.of(patientDTO);
    }
}
