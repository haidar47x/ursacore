package com.ursacore.bootstrap;

import com.ursacore.repository.PatientRepository;
import com.ursacore.repository.SampleRepository;
import com.ursacore.service.PatientCsvService;
import com.ursacore.service.PatientCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.shaded.com.google.errorprone.annotations.Immutable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(PatientCsvServiceImpl.class)
class BootstrapDataTest {

    @Autowired
    SampleRepository sampleRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    PatientCsvService patientCsvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(sampleRepository, patientRepository, patientCsvService);
    }

    @Test
    void testRun() throws Exception {
        bootstrapData.run();
        assertThat(sampleRepository.count()).isEqualTo(3);
        assertThat(patientRepository.count()).isEqualTo(2503);
    }
}