package com.ursacore.service;

import com.ursacore.model.SampleDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SampleService {

    List<SampleDTO> listSamples();

    Optional<SampleDTO> getSampleById(UUID sampleId);

    SampleDTO saveNewSample(SampleDTO sampleDTO);

    Optional<SampleDTO> updateSampleById(UUID sampleId, SampleDTO sampleDTO);

    void deleteById(UUID sampleId);

    void patchSampleById(UUID sampleId, SampleDTO sampleDTO);
}
