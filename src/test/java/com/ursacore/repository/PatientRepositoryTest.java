package com.ursacore.repository;

import com.ursacore.entity.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    PatientRepository patientRepository;

    @Test
    void testSavePatient() {
        Patient patient = Patient.builder()
                .name("Doe John")
                .build();

        var saved = patientRepository.save(patient);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(patient.getName());
    }
}