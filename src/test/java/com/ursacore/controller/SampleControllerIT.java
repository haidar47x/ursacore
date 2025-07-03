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
        List<SampleDTO> dtos = sampleController.listSamples();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        sampleRepository.deleteAll();
        List<SampleDTO> dtos = sampleController.listSamples();

        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testGetSampleById() {
        var sample = sampleRepository.findAll().getFirst();
        var dto = sampleController.getSampleById(sample.getId());

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(sample.getId());
    }

    @Test
    void testGetSampleByIdNotFound() {
        assertThrows(NotFoundException.class, () -> sampleController.getSampleById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewSample() {
        SampleDTO dto = SampleDTO.builder()
                .sampleCode("202A")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
            .build();

        var responseEntity = sampleController.createSample(dto);
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
}