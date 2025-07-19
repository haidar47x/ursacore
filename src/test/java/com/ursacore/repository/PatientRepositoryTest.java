package com.ursacore.repository;

import com.ursacore.bootstrap.BootstrapData;
import com.ursacore.entity.Patient;
import com.ursacore.entity.Sample;
import com.ursacore.model.BloodType;
import com.ursacore.model.Gender;
import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import com.ursacore.service.PatientCsvServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BootstrapData.class, PatientCsvServiceImpl.class})
class PatientRepositoryTest {

    @Autowired
    PatientRepository patientRepository;

    @Rollback
    @Transactional
    @Test
    void testSavePatient() {
        var patient = Patient.builder()
                .name("Doe John")
                .age(30)
                .gender(Gender.MALE)
                .bloodType(BloodType.A_NEGATIVE)
                .medicalCondition("Diabetes")
                .hospital("General Hospital")
                .build();

        var saved = patientRepository.save(patient);
        patientRepository.flush();

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo(patient.getName());
        assertThat(saved.getBloodType()).isEqualTo(patient.getBloodType());
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

    @Test
    void listPatientsByName() {
        var patients = patientRepository.findAllByNameIsLikeIgnoreCase("%Jon%");
        assertThat(patients).isNotNull();
        assertThat(patients).isNotEmpty();
        assertThat(patients.size()).isEqualTo(42);
    }
}