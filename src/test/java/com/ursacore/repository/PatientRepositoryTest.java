package com.ursacore.repository;

import com.ursacore.entity.Patient;
import com.ursacore.entity.Sample;
import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    PatientRepository patientRepository;

    @Test
    void testSavePatient() {
        var patient = Patient.builder()
                .name("Doe John")
                .build();

        var saved = patientRepository.save(patient);
        patientRepository.flush();

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(patient.getName());
    }

    @Test
    void testSavePatientNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            // name.length * 36 > 128
            var patient = Patient.builder()
                    .name("John Doe".repeat(36))
                    .build();

            patientRepository.save(patient);
            patientRepository.flush();
        });
    }
}