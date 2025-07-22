package com.ursacore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ursacore.exception.NotFoundException;
import com.ursacore.mapper.SampleMapper;
import com.ursacore.model.SampleDTO;
import com.ursacore.model.TestType;
import com.ursacore.repository.SampleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SampleControllerIT {

    @Autowired
    SampleController sampleController;
    
    @Autowired
    SampleRepository sampleRepository;

    @Autowired
    SampleMapper sampleMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testListSamples() {
        List<SampleDTO> sampleDtos = sampleController.listSamples();

        assertThat(sampleDtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testListSamplesEmpty() {
        sampleRepository.deleteAll();
        List<SampleDTO> sampleDtos = sampleController.listSamples();

        assertThat(sampleDtos.size()).isEqualTo(0);
    }

    @Test
    void testGetSampleById() {
        var sample = sampleRepository.findAll().getFirst();
        var sampleDto = sampleController.getSampleById(sample.getId());

        assertThat(sampleDto).isNotNull();
        assertThat(sampleDto.getId()).isEqualTo(sample.getId());
    }

    @Test
    void testGetSampleByIdNotFound() {
        assertThrows(NotFoundException.class, () -> sampleController.getSampleById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testCreateNewSample() {
        var sampleDtoCode = "202A";
        var sampleDto = SampleDTO.builder()
                .sampleCode(sampleDtoCode)
                .type(TestType.BLOOD)
            .build();

        var responseEntity = sampleController.createSample(sampleDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        var savedUri = responseEntity.getHeaders().getLocation().getPath();
        var savedUuid = UUID.fromString(savedUri.split("/")[4]);
        var sample = sampleRepository.findById(savedUuid).get();
        assertThat(sample).isNotNull();
        assertThat(sample.getSampleCode()).isEqualTo(sampleDtoCode);
    }

    @Test
    void testCreateNewSampleValidation() throws Exception {
        var sampleDto = SampleDTO.builder().
                    id(UUID.randomUUID())
                .build();

        mockMvc.perform(post(SampleController.SAMPLE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(4)));
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateSampleById() {
        var sample = sampleRepository.findAll().getFirst();
        var sampleDto = sampleMapper.sampleToSampleDto(sample);
        final var sampleDtoCode = "832F";
        sampleDto.setId(null);
        sampleDto.setVersion(null);
        sampleDto.setSampleCode(sampleDtoCode);

        var responseEntity = sampleController.updateSampleById(sample.getId(), sampleDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var savedSample = sampleRepository.findById(sample.getId()).get();
        assertThat(savedSample.getSampleCode()).isEqualTo(sampleDtoCode);
    }

    @Test
    void testUpdateSampleByIdValidation() throws Exception {
        final var sampleDtoCode = "832F".repeat(36);
        var sample = sampleRepository.findAll().getFirst();
        var sampleDto = sampleMapper.sampleToSampleDto(sample);
        sampleDto.setId(null);
        sampleDto.setVersion(null);
        sampleDto.setSampleCode(sampleDtoCode);

        mockMvc.perform(put(SampleController.SAMPLE_PATH_ID, sample.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void testUpdateSampleByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            sampleController.updateSampleById(UUID.randomUUID(),
                    SampleDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteSampleById() {
        var sample = sampleRepository.findAll().getFirst();
        var responseEntity = sampleController.deleteSampleById(sample.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(sampleRepository.findById(sample.getId())).isEmpty();
    }

    @Test
    void testDeleteSampleByIdNotFound() {
        assertThrows(NotFoundException.class, () -> sampleController.deleteSampleById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testPatchSampleById() {
        var sample = sampleRepository.findAll().getFirst();
        var sampleDto = sampleMapper.sampleToSampleDto(sample);
        final var sampleDtoCode = "TEST";
        sampleDto.setSampleCode(sampleDtoCode);

        var responseEntity = sampleController.patchSampleById(sampleDto.getId(), sampleDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var patchedSample = sampleRepository.findById(sample.getId()).get();
        assertThat(patchedSample).isNotNull();
        assertThat(patchedSample.getSampleCode()).isEqualTo(sampleDtoCode);
    }

    @Test
    void testPatchSampleByIdNotFound() {
        assertThrows(NotFoundException.class, () -> sampleController.patchSampleById(UUID.randomUUID(), SampleDTO.builder().build()));
    }
}