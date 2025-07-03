package com.ursacore.service;

import com.ursacore.mapper.PatientMapper;
import com.ursacore.model.PatientDTO;
import com.ursacore.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
@AllArgsConstructor
public class PatientServiceJPA implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public List<PatientDTO> listPatients() {
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PatientDTO> getPatientById(UUID patientId) {
        return Optional.ofNullable(
                patientMapper.patientToPatientDto(
                        patientRepository.findById(patientId).orElse(null)));
    }

    @Override
    public PatientDTO createNewPatient(PatientDTO patientDTO) {
        return null;
    }

    @Override
    public void updatePatientById(UUID patientId, PatientDTO patientDTO) {

    }

    @Override
    public void deleteById(UUID patientId) {

    }

    @Override
    public void patchPatientById(UUID patientId, PatientDTO patientDTO) {

    }
}
