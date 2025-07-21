package com.ursacore.service;

import com.ursacore.entity.Patient;
import com.ursacore.mapper.PatientMapper;
import com.ursacore.model.BloodType;
import com.ursacore.model.PatientDTO;
import com.ursacore.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Primary
@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final static int DEFAULT_PAGE_SIZE = 25;
    private final static int DEFAULT_PAGE_NUMBER = 0;

    @Override
    public Page<PatientDTO> listPatients(String name, BloodType bloodType, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Patient> patientPage;
        if (StringUtils.hasText(name) && bloodType == null) {
            patientPage = listPatientsByName(name, pageRequest);
        } else if (!StringUtils.hasText(name) && bloodType != null) {
            patientPage = listPatientsByBloodType(bloodType, pageRequest);
        } else if (StringUtils.hasText(name) && bloodType != null) {
            patientPage = listPatientsByNameAndBloodType(name, bloodType, pageRequest);
        } else {
            patientPage = patientRepository.findAll(pageRequest);
        }

        return patientPage.map(patientMapper::patientToPatientDto);
    }

    private Page<Patient> listPatientsByNameAndBloodType(String name, BloodType bloodType, Pageable pageable) {
        return patientRepository.findAllByNameIsLikeIgnoreCaseAndBloodType(
                "%" + name + "%", bloodType, pageable);
    }

    private Page<Patient> listPatientsByName(String name, Pageable pageable) {
        return patientRepository.findAllByNameIsLikeIgnoreCase("%" + name + "%", pageable);
    }

    private Page<Patient> listPatientsByBloodType(BloodType type, Pageable pageable) {
        return patientRepository.findAllByBloodType(type, pageable);
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNum;
        int queryPageSize;

        if (pageNumber == null && pageSize == null) {
            return PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        }

        if (pageNumber != null && pageNumber > 0) {
            queryPageNum = pageNumber - 1;
        } else {
            queryPageNum = DEFAULT_PAGE_NUMBER;
        }

        // Limit the page size to a maximum of 250
        // to prevent performance and memory overload
        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            // Will probably need a logging statement here
            // for future debugging and monitoring
            queryPageSize = pageSize > 250 ? 250 : pageSize;
        }

        return PageRequest.of(queryPageNum, queryPageSize, Sort.by(Sort.Order.asc("name")));
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
