package com.ursacore.repository;

import com.ursacore.entity.Patient;
import com.ursacore.entity.Sample;
import com.ursacore.entity.TestOrder;
import com.ursacore.model.TestType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class TestOrderRepositoryTest {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    SampleRepository sampleRepository;

    @Autowired
    TestOrderRepository testOrderRepository;

    Patient patient;
    Sample sample;

    @BeforeEach
    void setUp() {
        patient = patientRepository.findAll().getFirst();
        sample = sampleRepository.findAll().getFirst();
    }

    @Test
    void testTestOrderRepositoryExists() {
        assertThat(testOrderRepository.count()).isZero();
    }

    @Test
    void testCreateTestOrder() {
        var testOrder = TestOrder.builder()
                    .testType(TestType.BLOOD)
                    .patientRef("a patient reference")
                    .patient(patient)
                .build();

        var savedOrder = testOrderRepository.save(testOrder);
        log.info(savedOrder.getPatientRef());
    }

}