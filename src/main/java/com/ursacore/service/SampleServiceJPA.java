package com.ursacore.service;

import com.ursacore.model.SampleDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Primary
@Service
public class SampleServiceJPA implements SampleService {
    @Override
    public List<SampleDTO> listSamples() {
        return List.of();
    }

    @Override
    public Optional<SampleDTO> getSampleById(UUID sampleId) {
        return Optional.empty();
    }

    @Override
    public SampleDTO saveNewSample(SampleDTO sampleDTO) {
        return null;
    }

    @Override
    public void updateSampleById(UUID sampleId, SampleDTO sampleDTO) {

    }

    @Override
    public void deleteById(UUID sampleId) {

    }

    @Override
    public void patchSampleById(UUID sampleId, SampleDTO sampleDTO) {

    }
}
