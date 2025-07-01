package com.ursacore.service;

import com.ursacore.model.Sample;
import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Primary
@Service
public class SampleServiceImpl implements SampleService {

    private final Map<UUID, Sample> sampleMap;

    public SampleServiceImpl() {
        Sample s1 = Sample.builder()
                .id(UUID.randomUUID())
                .version(1)
                .sampleCode("87ED")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        Sample s2 = Sample.builder()
                .id(UUID.randomUUID())
                .version(1)
                .sampleCode("82ED")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        Sample s3 = Sample.builder()
                .id(UUID.randomUUID())
                .version(1)
                .sampleCode("85ED")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        this.sampleMap = new HashMap<>();
        sampleMap.put(s1.getId(), s1);
        sampleMap.put(s2.getId(), s2);
        sampleMap.put(s3.getId(), s3);
    }

    @Override
    public List<Sample> listSamples() {
        return new ArrayList<>(sampleMap.values());
    }

    @Override
    public Sample getSampleById(UUID sampleId) {
        return sampleMap.get(sampleId);
    }

    @Override
    public Sample saveNewSample(Sample sample) {
        Sample savedSample = Sample.builder()
                .id(UUID.randomUUID())
                .version(1)
                .sampleCode(sample.getSampleCode())
                .type(sample.getType())
                .status(sample.getStatus())
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        sampleMap.put(savedSample.getId(), savedSample);
        return savedSample;
    }

    @Override
    public void updateSampleById(UUID sampleId, Sample sample) {
        Sample existing = sampleMap.get(sampleId);
        existing.setUpdateAt(LocalDateTime.now());
        existing.setStatus(sample.getStatus());
        existing.setType(sample.getType());
        existing.setSampleCode(sample.getSampleCode());
    }

    @Override
    public void deleteById(UUID sampleId) {
        sampleMap.remove(sampleId);
    }

    @Override
    public void patchSampleById(UUID sampleId, Sample sample) {
        Sample existing = sampleMap.get(sampleId);
        if (sample.getSampleCode() != null) {
            existing.setSampleCode(sample.getSampleCode());
        }

        if (sample.getStatus() != null) {
            existing.setStatus(sample.getStatus());
        }

        if (sample.getType() != null) {
            existing.setType(sample.getType());
        }
    }
}
