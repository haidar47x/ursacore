package com.ursacore.service;

import com.ursacore.model.SampleDTO;
import com.ursacore.model.SampleStatus;
import com.ursacore.model.SampleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class SampleServiceImpl implements SampleService {

    private final Map<UUID, SampleDTO> sampleMap;

    public SampleServiceImpl() {
        SampleDTO s1 = SampleDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .sampleCode("87ED")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        SampleDTO s2 = SampleDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .sampleCode("82ED")
                .status(SampleStatus.PROCESSING)
                .type(SampleType.BLOOD_TEST)
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        SampleDTO s3 = SampleDTO.builder()
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
    public List<SampleDTO> listSamples() {
        return new ArrayList<>(sampleMap.values());
    }

    @Override
    public Optional<SampleDTO> getSampleById(UUID sampleId) {
        return Optional.of(sampleMap.get(sampleId));
    }

    @Override
    public SampleDTO saveNewSample(SampleDTO sampleDTO) {
        SampleDTO savedSampleDTO = SampleDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .sampleCode(sampleDTO.getSampleCode())
                .type(sampleDTO.getType())
                .status(sampleDTO.getStatus())
                .collectedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
            .build();

        sampleMap.put(savedSampleDTO.getId(), savedSampleDTO);
        return savedSampleDTO;
    }

    @Override
    public Optional<SampleDTO> updateSampleById(UUID sampleId, SampleDTO sampleDTO) {
        SampleDTO existing = sampleMap.get(sampleId);
        if (existing == null) {
            return Optional.empty();
        }
        existing.setUpdateAt(LocalDateTime.now());
        existing.setStatus(sampleDTO.getStatus());
        existing.setType(sampleDTO.getType());
        existing.setSampleCode(sampleDTO.getSampleCode());
        return Optional.of(existing);
    }

    @Override
    public Boolean deleteSampleById(UUID sampleId) {
        sampleMap.remove(sampleId);
        return true;
    }

    @Override
    public Optional<SampleDTO> patchSampleById(UUID sampleId, SampleDTO sampleDTO) {
        SampleDTO existing = sampleMap.get(sampleId);

        if (existing == null) {
            return Optional.empty();
        }

        if (sampleDTO.getSampleCode() != null) {
            existing.setSampleCode(sampleDTO.getSampleCode());
        }

        if (sampleDTO.getStatus() != null) {
            existing.setStatus(sampleDTO.getStatus());
        }

        if (sampleDTO.getType() != null) {
            existing.setType(sampleDTO.getType());
        }

        return Optional.of(sampleDTO);
    }
}
