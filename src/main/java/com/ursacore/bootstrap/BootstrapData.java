package com.ursacore.bootstrap;

import com.ursacore.entity.Patient;
import com.ursacore.entity.Sample;
import com.ursacore.model.BloodType;
import com.ursacore.model.Gender;
import com.ursacore.model.PatientCsvRecord;
import com.ursacore.model.SampleType;
import com.ursacore.repository.PatientRepository;
import com.ursacore.repository.SampleRepository;
import com.ursacore.service.PatientCsvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BootstrapData implements CommandLineRunner {

    private final SampleRepository sampleRepository;
    private final PatientRepository patientRepository;
    private final PatientCsvService patientCsvService;

    @Override
    public void run(String... args) throws Exception {
        initSampleData();
        initPatientData();
        initPatientCsvData();
    }

    public void initPatientCsvData() throws FileNotFoundException {
        log.info("Checking if patient data already exists...");
        log.info("Current patient count: {}", patientRepository.count());
        if (patientRepository.count() > 10) return;

        File file = ResourceUtils.getFile("classpath:csvdata/patients.csv");
        List<PatientCsvRecord> records = patientCsvService.convertCsv(file);

        if (records == null || records.isEmpty()) {
            log.warn("No patient records found in CSV file.");
            return;
        }

        records.forEach(record -> {
            try {
                Gender gender = switch (record.getGender().toLowerCase()) {
                    case "male" -> Gender.MALE;
                    case "female" -> Gender.FEMALE;
                    default -> Gender.OTHER;
                };

                BloodType bloodType = switch (record.getBloodType().toLowerCase()) {
                    case "a+" -> BloodType.A_POSITIVE;
                    case "a-" -> BloodType.A_NEGATIVE;
                    case "b+" -> BloodType.B_POSITIVE;
                    case "b-" -> BloodType.B_NEGATIVE;
                    case "ab+" -> BloodType.AB_POSITIVE;
                    case "ab-" -> BloodType.AB_NEGATIVE;
                    case "o+" -> BloodType.O_POSITIVE;
                    case "o-" -> BloodType.O_NEGATIVE;
                    default -> BloodType.UNKNOWN;
                };

                Patient patient = Patient.builder()
                        .name(record.getName())
                        .age(record.getAge())
                        .gender(gender)
                        .bloodType(bloodType)
                        .doctor(record.getDoctor())
                        .medicalCondition(record.getMedicalCondition())
                        .hospital(record.getHospital())
                        .createdAt(LocalDateTime.now())
                        .updateAt(LocalDateTime.now())
                        .build();

                patientRepository.save(patient);
            } catch (Exception e) {
                log.error("Error saving patient record: {}", record, e);
            }
        });

        log.info("{} patient records imported from CSV file.", records.size());
    }

    public void initSampleData() {

        if (sampleRepository.count() > 0) return;

        Sample s1 = Sample.builder()
                .sampleCode("87ED")
                .sampleType(SampleType.BLOOD)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        Sample s2 = Sample.builder()
                .sampleCode("82ED")
                .sampleType(SampleType.BLOOD)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        Sample s3 = Sample.builder()
                .sampleCode("85ED")
                .sampleType(SampleType.BLOOD)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        sampleRepository.saveAll(List.of(s1, s2, s3));
    }

    public void initPatientData() {

        if (patientRepository.count() > 0) return;

        Patient p1 = Patient.builder()
                .name("John Doe")
                .age(29)
                .gender(Gender.MALE)
                .bloodType(BloodType.A_NEGATIVE)
                .doctor("Dr. Doe")
                .medicalCondition("Flu")
                .hospital("City Hospital")
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        Patient p2 = Patient.builder()
                .name("Jon Blow")
                .age(34)
                .gender(Gender.FEMALE)
                .bloodType(BloodType.B_POSITIVE)
                .doctor("Dr. Smith")
                .medicalCondition("Hypertension")
                .hospital("General Hospital")
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        Patient p3 = Patient.builder()
                .name("Jane Bow")
                .age(28)
                .gender(Gender.FEMALE)
                .bloodType(BloodType.A_NEGATIVE)
                .doctor("Dr. Ali")
                .medicalCondition("Diabetes")
                .hospital("Community Hospital")
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        patientRepository.saveAll(List.of(p1, p2, p3));
    }
}
