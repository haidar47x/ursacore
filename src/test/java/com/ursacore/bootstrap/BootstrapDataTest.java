package com.ursacore.bootstrap;

import com.ursacore.repository.PatientRepository;
import com.ursacore.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BootstrapDataTest {

    @Autowired
    SampleRepository sampleRepository;

    @Autowired
    PatientRepository patientRepository;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(sampleRepository, patientRepository);
    }

    @Test
    void testRun() throws Exception {
        bootstrapData.run();
        assertThat(sampleRepository.count()).isEqualTo(3);
        assertThat(patientRepository.count()).isEqualTo(3);
    }
}