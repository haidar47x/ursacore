package com.ursacore.service;

import com.ursacore.mapper.SampleMapper;
import com.ursacore.model.SampleDTO;
import com.ursacore.repository.SampleRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Primary
@Service
@AllArgsConstructor
public class SampleServiceJPA implements SampleService {

    private final SampleRepository sampleRepository;
    private final SampleMapper sampleMapper;

    @Override
    public List<SampleDTO> listSamples() {
        return sampleRepository.findAll()
                .stream()
                .map(sampleMapper::sampleToSampleDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<SampleDTO> getSampleById(UUID sampleId) {
        return Optional.ofNullable(
                sampleMapper.sampleToSampleDto(
                        sampleRepository.findById(sampleId).orElse(null)));
    }

    @Override
    public SampleDTO saveNewSample(SampleDTO sampleDTO) {
        return sampleMapper.sampleToSampleDto(sampleRepository.save(sampleMapper.sampleDtoToSample(sampleDTO)));
    }

    @Override
    public Optional<SampleDTO> updateSampleById(UUID sampleId, SampleDTO sampleDTO) {
        AtomicReference<Optional<SampleDTO>> atomicReference = new AtomicReference<>();

        sampleRepository.findById(sampleId).ifPresentOrElse(foundSample -> {
            foundSample.setSampleCode(sampleDTO.getSampleCode());
            foundSample.setStatus(sampleDTO.getStatus());
            foundSample.setType(sampleDTO.getType());
            foundSample.setCollectedAt(sampleDTO.getCollectedAt());
            var savedSample = sampleRepository.save(foundSample);
            atomicReference.set(Optional.of(sampleMapper.sampleToSampleDto(savedSample)));
        }, () -> atomicReference.set(Optional.empty()));

        return atomicReference.get();
    }

    @Override
    public void deleteById(UUID sampleId) {

    }

    @Override
    public void patchSampleById(UUID sampleId, SampleDTO sampleDTO) {

    }
}
