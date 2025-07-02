package com.ursacore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ursacore.model.Patient;
import com.ursacore.model.Sample;
import com.ursacore.service.PatientService;
import com.ursacore.service.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

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

    PatientService patientServiceImpl = new PatientServiceImpl();

    @Test
    void listPatients() throws Exception {
        given(patientService.listPatients()).willReturn(patientServiceImpl.listPatients());

        mockMvc.perform(get("/api/v1/patient")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getPatientById() throws Exception {
        Patient testPatient = patientServiceImpl.listPatients().getFirst();
        given(patientService.getPatientById(testPatient.getId())).willReturn(testPatient);

        mockMvc.perform(get("/api/v1/patient/" + testPatient.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testPatient.getId().toString())))
                .andExpect(jsonPath("$.name", is(testPatient.getName())));
    }

    @Test
    void testCreateNewPatient() throws Exception {
        Patient patient = patientServiceImpl.listPatients().getFirst();
        patient.setVersion(null);
        patient.setId(null);
        given(patientService.createNewPatient(any(Patient.class))).willReturn(patientServiceImpl.listPatients().get(1));

        mockMvc.perform(post("/api/v1/sample")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdatePatient() throws Exception {
        Patient patient = patientServiceImpl.listPatients().getFirst();

        mockMvc.perform(put("/api/v1/patient/" + patient.getId().toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isNoContent());

        verify(patientService).updatePatientById(eq(patient.getId()), any(Patient.class));
    }

    @Test
    void testDeletePatient() throws Exception {
        Patient patient = patientServiceImpl.listPatients().getFirst();

        mockMvc.perform(delete("/api/v1/patient/" + patient.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(patientService).deleteById(uuidArgumentCaptor.capture());

        assertThat(patient.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }
}