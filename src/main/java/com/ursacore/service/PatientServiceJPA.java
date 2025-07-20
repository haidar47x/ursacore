package com.ursacore.service;

import com.ursacore.entity.Patient;
import com.ursacore.mapper.PatientMapper;
import com.ursacore.model.BloodType;
import com.ursacore.model.PatientDTO;
import com.ursacore.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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

    // TODO: Refactor to specifications
    @Override
    public List<PatientDTO> listPatients(String name, BloodType bloodType) {
        List<Patient> patients;
        if (StringUtils.hasText(name) && bloodType == null) {
            patients = listPatientsByName(name);
        } else if (!StringUtils.hasText(name) && bloodType != null) {
            patients = listPatientsByBloodType(bloodType);
        } else if (StringUtils.hasText(name) && bloodType != null) {
            patients = listPatientsByNameAndBloodType(name, bloodType);
        } else {
            patients = patientRepository.findAll();
        }

        return patients
                .stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    private List<Patient> listPatientsByNameAndBloodType(String name, BloodType bloodType) {
        return patientRepository.findAllByNameIsLikeIgnoreCaseAndBloodType(
                "%" + name + "%", bloodType);
    }

    private List<Patient> listPatientsByName(String name) {
        return patientRepository.findAllByNameIsLikeIgnoreCase("%" + name + "%");
    }

    private List<Patient> listPatientsByBloodType(BloodType type) {
        return patientRepository.findAllByBloodType(type);
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
    public Boolean deletePatientById(UUID patientId) {
        if (!patientRepository.existsById(patientId)) {
            return false;
        }
        patientRepository.deleteById(patientId);
        return true;
    }

    @Override
    public Optional<PatientDTO> patchPatientById(UUID patientId, PatientDTO patientDTO) {
        if (!patientRepository.existsById(patientId))
            return Optional.empty();

        Patient existing = patientRepository.findById(patientId).get();
        if (patientDTO.getName() != null) {
            existing.setName(patientDTO.getName());
        }

        PatientDTO saved = patientMapper.patientToPatientDto(patientRepository.save(existing));
        return Optional.of(saved);
    }
}
