package com.ursacore.service;

import com.ursacore.entity.Patient;
import com.ursacore.mapper.PatientMapper;
import com.ursacore.model.PatientDTO;
import com.ursacore.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
    public PatientDTO saveNewPatient(PatientDTO patientDto) {
        Patient savedPatient = patientRepository.save(
                patientMapper.patientDtoToPatient(patientDto));
        return patientMapper.patientToPatientDto(savedPatient);
    }

    @Override
    public Optional<PatientDTO> updatePatientById(UUID patientId, PatientDTO patientDto) {
        AtomicReference<Optional<PatientDTO>> atomicReference = new AtomicReference<>();
        patientRepository.findById(patientId).ifPresentOrElse(foundPatient -> {
            foundPatient.setName(patientDto.getName());
            Patient savedPatient = patientRepository.save(foundPatient);
            atomicReference.set(Optional.of(
                    patientMapper.patientToPatientDto(savedPatient)));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public void deleteById(UUID patientId) {

    }

    @Override
    public void patchPatientById(UUID patientId, PatientDTO patientDTO) {

    }
}
