package com.ursacore.controller;

import com.ursacore.exception.NotFoundException;
import com.ursacore.mapper.PatientMapper;
import com.ursacore.model.PatientDTO;
import com.ursacore.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PatientControllerIT {

    @Autowired
    PatientController patientController;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientMapper patientMapper;

    @Test
    void testListPatients() {
        List<PatientDTO> patientDtos = patientController.listPatients();

        assertThat(patientDtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testListPatientsEmpty() {
        patientRepository.deleteAll();
        List<PatientDTO> patientDtos = patientController.listPatients();

        assertThat(patientDtos.size()).isEqualTo(0);
    }

    @Test
    void testGetPatientById() {
        var patient = patientRepository.findAll().getFirst();
        var patientDto = patientController.getPatientById(patient.getId());

        assertThat(patientDto).isNotNull();
        assertThat(patient.getId()).isEqualTo(patientDto.getId());
    }

    @Test
    void testGetPatientByIdNotFound() {
        assertThrows(NotFoundException.class, () -> patientController.getPatientById(UUID.randomUUID()));
    }

    @Test
    void testCreateNewPatient() {
        var patientName = "Doe John";
        var patientDto = PatientDTO.builder()
                .name(patientName)
                .build();
        var responseEntity = patientController.createPatient(patientDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        var savedUri = responseEntity.getHeaders().getLocation().getPath();
        var savedUuid = UUID.fromString(savedUri.split("/")[4]);
        var patient = patientRepository.findById(savedUuid).get();

        assertThat(patient).isNotNull();
        assertThat(patient.getName()).isEqualTo(patientName);
    }

    @Rollback
    @Transactional
    @Test
    void testUpdatePatientById() {
        var patient = patientRepository.findAll().getFirst();
        var patientDto = patientMapper.patientToPatientDto(patient);
        final var patientName = "Doe John";
        patientDto.setId(null);
        patientDto.setVersion(null);
        patientDto.setName(patientName);

        var responseEntity = patientController.updatePatientById(patient.getId(), patientDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var updatedPatient = patientRepository.findById(patient.getId()).get();
        assertThat(updatedPatient.getName()).isEqualTo(patientName);
    }

    @Test
    void testUpdatePatientByIdNotFound() {
        assertThrows(NotFoundException.class, () -> patientController.updatePatientById(
                UUID.randomUUID(), PatientDTO.builder().build()));
    }

    @Rollback
    @Transactional
    @Test
    void testDeletePatientById() {
        var patient = patientRepository.findAll().getFirst();
        var responseEntity = patientController.deletePatientById(patient.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(patientRepository.findById(patient.getId())).isEmpty();
    }

    @Test
    void testDeletePatientByIdNotFound() {
        assertThrows(NotFoundException.class, () -> patientController.deletePatientById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testPatchPatientById() {
        var patient = patientRepository.findAll().getFirst();
        var patientDto = patientMapper.patientToPatientDto(patient);
        final var patientName = "Doe John";
        patientDto.setName(patientName);

        var responseEntity = patientController.patchPatientById(patient.getId(), patientDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var patchedPatient = patientRepository.findById(patient.getId()).get();
        assertThat(patchedPatient).isNotNull();
        assertThat(patchedPatient.getName()).isEqualTo(patientName);
    }

    @Test
    void testPatchPatientByIdNotFound() {
        assertThrows(NotFoundException.class, () -> patientController.patchPatientById(UUID.randomUUID(), PatientDTO.builder().build()));
    }
}