package com.ursacore.controller;

import com.ursacore.entity.Sample;
import com.ursacore.exception.NotFoundException;
import com.ursacore.mapper.SampleMapper;
import com.ursacore.model.SampleDTO;
import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import com.ursacore.repository.SampleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SampleControllerIT {

    @Autowired
    SampleController sampleController;
    
    @Autowired
    SampleRepository sampleRepository;

    @Autowired
    SampleMapper sampleMapper;

    @Test
    void testListSamples() {
        List<SampleDTO> sampleDtos = sampleController.listSamples();

        assertThat(sampleDtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
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
    void testSaveNewSample() {
        SampleDTO sampleDto = SampleDTO.builder()
                .sampleCode("202A")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
            .build();

        var responseEntity = sampleController.createSample(sampleDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        var savedUri = responseEntity.getHeaders().getLocation().getPath();
        var savedUuid = UUID.fromString(savedUri.split("/")[4]);
        var savedSample = sampleRepository.findById(savedUuid);
        assertThat(savedSample).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateSampleById() {
        var sample = sampleRepository.findAll().getFirst();
        var sampleDto = sampleMapper.sampleToSampleDto(sample);
        sampleDto.setId(null);
        sampleDto.setVersion(null);

        final String sampleDtoCode = "832F";
        sampleDto.setSampleCode(sampleDtoCode);

        var responseEntity = sampleController.updateSampleById(sample.getId(), sampleDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var savedSample = sampleRepository.findById(sample.getId()).get();
        assertThat(savedSample.getSampleCode()).isEqualTo("832F");
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
        var responseEntity = sampleController.deleteById(sample.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(sampleRepository.findById(sample.getId())).isEmpty();
    }

    @Test
    void testDeleteSampleByIdNotFound() {
        assertThrows(NotFoundException.class, () -> sampleController.deleteById(UUID.randomUUID()));
    }
}