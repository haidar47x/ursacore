package com.ursacore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ursacore.entity.Patient;
import com.ursacore.mapper.PatientMapper;
import com.ursacore.model.BloodType;
import com.ursacore.model.Gender;
import com.ursacore.model.PatientDTO;
import com.ursacore.repository.PatientRepository;
import com.ursacore.service.PatientService;
import com.ursacore.service.PatientServiceImpl;
import com.ursacore.service.PatientServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PatientService patientService;

    @Captor
    ArgumentCaptor<PatientDTO> patientArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Test
    void testListPatients() throws Exception {
        var patientList = List.of(
                PatientDTO.builder().name("A").build(),
                PatientDTO.builder().name("B").build(),
                PatientDTO.builder().name("C").build()
        );

        given(patientService.listPatients()).willReturn(patientList);

        mockMvc.perform(get(PatientController.PATIENT_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testGetPatientById() throws Exception {
        var patientDto = PatientDTO.builder().id(UUID.randomUUID()).name("John Doe").build();
        given(patientService.getPatientById(patientDto.getId())).willReturn(Optional.of(patientDto));

        mockMvc.perform(get(PatientController.PATIENT_PATH_ID, patientDto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(patientDto.getId().toString())))
                .andExpect(jsonPath("$.name", is(patientDto.getName())));
    }

    @Test
    void testGetPatientByIdNotFound() throws Exception {
        given(patientService.getPatientById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(PatientController.PATIENT_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateNewPatient() throws Exception {
        var patientDto  = PatientDTO.builder()
                    .id(UUID.randomUUID())
                    .name("John Doe")
                    .age(30)
                    .gender(Gender.MALE)
                    .bloodType(BloodType.A_NEGATIVE)
                    .doctor("Dr. Khan")
                    .medicalCondition("Seborrheic Dermatitis")
                    .hospital("Community Hospital")
                .build();
        given(patientService.saveNewPatient(patientDto)).willReturn(patientDto);

        mockMvc.perform(post(PatientController.PATIENT_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testCreateNewPatientValidation() throws Exception {
        var patientDTO = PatientDTO.builder().id(UUID.randomUUID()).build();
        given(patientService.saveNewPatient(any(PatientDTO.class))).willReturn(patientDTO);

        mockMvc.perform(post(PatientController.PATIENT_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(7)));
    }

    @Test
    void testUpdatePatientById() throws Exception {
        var patientDto  = PatientDTO.builder()
                    .id(UUID.randomUUID())
                    .name("John Doe")
                    .age(30)
                    .gender(Gender.MALE)
                    .bloodType(BloodType.A_NEGATIVE)
                    .doctor("Dr. Khan")
                    .medicalCondition("Seborrheic Dermatitis")
                    .hospital("Community Hospital")
                .build();
        
        given(patientService.updatePatientById(any(UUID.class), any(PatientDTO.class))).willReturn(Optional.of(patientDto));

        mockMvc.perform(put(PatientController.PATIENT_PATH_ID, patientDto.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDto)))
                .andExpect(status().isNoContent());

        verify(patientService).updatePatientById(eq(patientDto.getId()), any(PatientDTO.class));
    }

    @Test
    void testDeletePatientById() throws Exception {
        var patientDTO = PatientDTO.builder().id(UUID.randomUUID()).build();

        given(patientService.deletePatientById(any(UUID.class))).willReturn(true);

        mockMvc.perform(delete(PatientController.PATIENT_PATH_ID, patientDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(patientService).deletePatientById(uuidArgumentCaptor.capture());
        assertThat(patientDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testDeletePatientByIdNotFound() throws Exception {
        given(patientService.deletePatientById(any(UUID.class))).willReturn(false);

        mockMvc.perform(delete(PatientController.PATIENT_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPatchPatientById() throws Exception {
        PatientDTO patientDTO = PatientDTO.builder().id(UUID.randomUUID()).name("John Doe").build();

        given(patientService.patchPatientById(any(UUID.class), any(PatientDTO.class))).willReturn(Optional.of(patientDTO));

        mockMvc.perform(patch(PatientController.PATIENT_PATH_ID, patientDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isNoContent());

        verify(patientService).patchPatientById(uuidArgumentCaptor.capture(), patientArgumentCaptor.capture());
        assertThat(patientDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(patientDTO.getName()).isEqualTo(patientArgumentCaptor.getValue().getName());
    }

    @Test
    void testPatchPatientByIdNotFound() throws Exception {
        given(patientService.patchPatientById(any(UUID.class), any(PatientDTO.class))).willReturn(Optional.empty());

        mockMvc.perform(patch(PatientController.PATIENT_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(PatientDTO.builder().build())))
                .andExpect(status().isNotFound());
    }
}