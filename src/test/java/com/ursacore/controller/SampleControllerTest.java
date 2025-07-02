package com.ursacore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ursacore.model.SampleDTO;
import com.ursacore.service.SampleService;
import com.ursacore.service.SampleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SampleController.class)
class SampleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    SampleService sampleService;

    @Captor
    ArgumentCaptor<SampleDTO> sampleArgumentCaptor;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    SampleServiceImpl sampleServiceImpl;

    @BeforeEach
    void setUp() {
        sampleServiceImpl = new SampleServiceImpl();
    }

    @Test
    void testListSamples() throws Exception {
        given(sampleService.listSamples()).willReturn(sampleServiceImpl.listSamples());

        mockMvc.perform(get(SampleController.SAMPLE_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testGetSampleById() throws Exception {
        SampleDTO testSampleDTO = sampleServiceImpl.listSamples().getFirst();
        given(sampleService.getSampleById(testSampleDTO.getId())).willReturn(Optional.of(testSampleDTO));

        mockMvc.perform(get(SampleController.SAMPLE_PATH_ID, testSampleDTO.getId().toString())
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testSampleDTO.getId().toString())))
                .andExpect(jsonPath("$.sampleCode", is(testSampleDTO.getSampleCode())));
    }

    @Test
    void testCreateNewSample() throws Exception {
        SampleDTO sampleDTO = sampleServiceImpl.listSamples().getFirst();
        /* Version and ID are null because we want to mimic a new Sample object. */
        sampleDTO.setVersion(null);
        sampleDTO.setId(null);
        given(sampleService.saveNewSample(any(SampleDTO.class))).willReturn(sampleServiceImpl.listSamples().get(1));

        mockMvc.perform(post(SampleController.SAMPLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleDTO)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateSample() throws Exception {
        SampleDTO sampleDTO = sampleServiceImpl.listSamples().getFirst();

        mockMvc.perform(put(SampleController.SAMPLE_PATH_ID, sampleDTO.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleDTO)))
                .andExpect(status().isNoContent());

        /* Verify updateSampleById was called with the given ID */
        verify(sampleService).updateSampleById(eq(sampleDTO.getId()), any(SampleDTO.class));
    }

    @Test
    void testDeleteSample() throws Exception {
        SampleDTO sampleDTO = sampleServiceImpl.listSamples().getFirst();

        mockMvc.perform(delete(SampleController.SAMPLE_PATH_ID, sampleDTO.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(sampleService).deleteById(uuidArgumentCaptor.capture());
        assertThat(sampleDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchSample() throws Exception {
        SampleDTO sampleDTO = sampleServiceImpl.listSamples().getFirst();
        Map<String, Object> sampleMap = new HashMap<>();
        sampleMap.put("sampleCode", "AA33");

        mockMvc.perform(patch(SampleController.SAMPLE_PATH_ID, sampleDTO.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sampleDTO)))
                .andExpect(status().isNoContent());
        /*
          The captors will contain values that were passed to the mocked service method.
          We want to make sure that all the critical parts are covered by the tests.
         */
        verify(sampleService).patchSampleById(uuidArgumentCaptor.capture(), sampleArgumentCaptor.capture());
        assertThat(sampleDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(sampleDTO.getSampleCode()).isEqualTo(sampleArgumentCaptor.getValue().getSampleCode());
    }

    @Test
    void testGetSampleByIdNotFound() throws Exception {
        given(sampleService.getSampleById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(SampleController.SAMPLE_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}