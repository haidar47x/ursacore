package com.ursacore.service;

import com.ursacore.model.Sample;

import java.util.List;
import java.util.UUID;

public interface SampleService {

    List<Sample> listSamples();

    Sample getSampleById(UUID sampleId);

    Sample saveNewSample(Sample sample);

    void updateSampleById(UUID sampleId, Sample sample);

    void deleteById(UUID sampleId);

    void patchSampleById(UUID sampleId, Sample sample);
}
