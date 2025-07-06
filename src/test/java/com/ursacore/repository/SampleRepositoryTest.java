package com.ursacore.repository;

import com.ursacore.entity.Sample;
import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SampleRepositoryTest {

    @Autowired
    SampleRepository sampleRepository;

    @Test
    void testSaveSample() {
        var sample = Sample.builder()
                .sampleCode("872A")
                .type(SampleType.BLOOD_TEST)
                .status(SampleStatus.PROCESSING)
                .collectedAt(LocalDateTime.now())
                .build();

        var saved = sampleRepository.save(sample);
        sampleRepository.flush();

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSampleCode()).isEqualTo(sample.getSampleCode());
        assertThat(saved.getType()).isEqualTo(sample.getType());
        assertThat(saved.getStatus()).isEqualTo(sample.getStatus());
    }

    @Test
    void testSaveSampleCodeTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            // 4 * 2 > 4
            var sample = Sample.builder()
                    .sampleCode("872A".repeat(2))
                    .type(SampleType.BLOOD_TEST)
                    .status(SampleStatus.PROCESSING)
                    .collectedAt(LocalDateTime.now())
                    .build();

            sampleRepository.save(sample);
            sampleRepository.flush();
        });
    }
}