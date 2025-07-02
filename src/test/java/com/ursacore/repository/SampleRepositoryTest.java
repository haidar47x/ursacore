package com.ursacore.repository;

import com.ursacore.entity.Sample;
import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SampleRepositoryTest {

    @Autowired
    SampleRepository sampleRepository;

    @Test
    void testSaveSample() {
        Sample sample = Sample.builder()
                .sampleCode("872A")
                .type(SampleType.BLOOD_TEST)
                .status(SampleStatus.PROCESSING)
                .build();

        var saved = sampleRepository.save(sample);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSampleCode()).isEqualTo(sample.getSampleCode());
        assertThat(saved.getType()).isEqualTo(sample.getType());
        assertThat(saved.getStatus()).isEqualTo(sample.getStatus());
    }
}