package com.ursacore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ursacore.entity.Sample;
import com.ursacore.mapper.SampleMapper;
import com.ursacore.model.SampleDTO;
import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import com.ursacore.repository.SampleRepository;
import com.ursacore.service.SampleService;
import com.ursacore.service.SampleServiceJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    @Test
    void testListSamples() throws Exception {
        var sampleList = List.of(
                SampleDTO.builder().sampleCode("123A").build(),
                SampleDTO.builder().sampleCode("132A").build(),
                SampleDTO.builder().sampleCode("144A").build()
        );

        given(sampleService.listSamples()).willReturn(sampleList);

        mockMvc.perform(get(SampleController.SAMPLE_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void testGetSampleById() throws Exception {
        var sampleDto = SampleDTO.builder().id(UUID.randomUUID()).sampleCode("182F").build();
        given(sampleService.getSampleById(sampleDto.getId())).willReturn(Optional.of(sampleDto));

        mockMvc.perform(get(SampleController.SAMPLE_PATH_ID, sampleDto.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(sampleDto.getId().toString())))
                .andExpect(jsonPath("$.sampleCode", is(sampleDto.getSampleCode())));
    }

    @Test
    void testGetSampleByIdNotFound() throws Exception {
        given(sampleService.getSampleById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(SampleController.SAMPLE_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateNewSample() throws Exception {
        var sampleDto = SampleDTO.builder()
                .id(UUID.randomUUID())
                .sampleCode("1890")
                .type(SampleType.BLOOD_TEST)
                .status(SampleStatus.PROCESSING)
                .collectedAt(LocalDateTime.now())
            .build();
        given(sampleService.saveNewSample(any(SampleDTO.class))).willReturn(sampleDto);

        mockMvc.perform(post(SampleController.SAMPLE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testCreateNewSampleValidation() throws Exception {
        var sampleDto = SampleDTO.builder().id(UUID.randomUUID()).build();
        given(sampleService.saveNewSample(any(SampleDTO.class))).willReturn(sampleDto);

        mockMvc.perform(post(SampleController.SAMPLE_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleDto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.length()", is(5)));
    }

    @Test
    void testUpdateSampleById() throws Exception {
        var sampleDTO = SampleDTO.builder()
                .id(UUID.randomUUID())
                .sampleCode("2722")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
                .collectedAt(LocalDateTime.now())
            .build();

        given(sampleService.updateSampleById(eq(sampleDTO.getId()), any(SampleDTO.class)))
                .willReturn(Optional.of(sampleDTO));

        mockMvc.perform(put(SampleController.SAMPLE_PATH_ID, sampleDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDTO)))
                .andExpect(status().isNoContent());

        // Verify updateSampleById was called with the given ID.
        verify(sampleService).updateSampleById(eq(sampleDTO.getId()), any(SampleDTO.class));
    }

    @Test
    void testUpdateSampleByIdValidation() throws Exception {
        var sampleDTO = SampleDTO.builder()
                .id(UUID.randomUUID())
                .build();

        given(sampleService.updateSampleById(eq(sampleDTO.getId()), any(SampleDTO.class)))
                .willReturn(Optional.of(sampleDTO));

        mockMvc.perform(put(SampleController.SAMPLE_PATH_ID, sampleDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(5)));

        // We omitted the verification because the test doesn't exercise the service method.
    }

    @Test
    void testUpdateSampleByIdNotFound() throws Exception {
        var sampleDTO = SampleDTO.builder()
                .id(UUID.randomUUID())
                .sampleCode("2722")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
                .collectedAt(LocalDateTime.now())
                .build();

        given(sampleService.updateSampleById(any(UUID.class), any(SampleDTO.class)))
                .willReturn(Optional.empty());

        mockMvc.perform(put(SampleController.SAMPLE_PATH_ID, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteSample() throws Exception {
        var randomUuid = UUID.randomUUID();

        given(sampleService.deleteSampleById(randomUuid)).willReturn(true);

        mockMvc.perform(delete(SampleController.SAMPLE_PATH_ID, randomUuid)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(sampleService).deleteSampleById(uuidArgumentCaptor.capture());
        assertThat(randomUuid).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testDeleteSampleNotFound() throws Exception {
        given(sampleService.deleteSampleById(any(UUID.class))).willReturn(false);

        mockMvc.perform(delete(SampleController.SAMPLE_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPatchSampleById() throws Exception {
        var sampleDTO = SampleDTO.builder().id(UUID.randomUUID()).sampleCode("55BA").build();

        given(sampleService.patchSampleById(sampleDTO.getId(), sampleDTO)).willReturn(Optional.of(sampleDTO));

        mockMvc.perform(patch(SampleController.SAMPLE_PATH_ID, sampleDTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testPatchSampleByIdNotFound() throws Exception {
        given(sampleService.patchSampleById(any(UUID.class), any(SampleDTO.class))).willReturn(Optional.empty());

        mockMvc.perform(patch(SampleController.SAMPLE_PATH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(SampleDTO.builder().build())))
                .andExpect(status().isNotFound());
    }
}