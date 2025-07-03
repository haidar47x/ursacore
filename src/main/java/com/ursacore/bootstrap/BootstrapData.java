package com.ursacore.bootstrap;

import com.ursacore.entity.Patient;
import com.ursacore.entity.Sample;
import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import com.ursacore.repository.PatientRepository;
import com.ursacore.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final SampleRepository sampleRepository;
    private final PatientRepository patientRepository;

    @Override
    public void run(String... args) throws Exception {
        initSampleData();
        initPatientData();
    }

    public void initSampleData() {
        Sample s1 = Sample.builder()
                .sampleCode("87ED")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        Sample s2 = Sample.builder()
                .sampleCode("82ED")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        Sample s3 = Sample.builder()
                .sampleCode("85ED")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        sampleRepository.save(s1);
        sampleRepository.save(s2);
        sampleRepository.save(s3);
    }

    public void initPatientData() {
        Patient p1 = Patient.builder()
                .name("John Doe")
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        Patient p2 = Patient.builder()
                .name("Jan Doe")
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        Patient p3 = Patient.builder()
                .name("Jonas Doe")
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        patientRepository.save(p1);
        patientRepository.save(p2);
        patientRepository.save(p3);
    }
}
