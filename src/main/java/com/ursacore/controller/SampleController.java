package com.ursacore.controller;

import com.ursacore.exception.NotFoundException;
import com.ursacore.model.SampleDTO;
import com.ursacore.service.SampleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@Slf4j
public class SampleController {

    public static final String SAMPLE_PATH = "/api/v1/sample";
    public static final String SAMPLE_PATH_ID = SAMPLE_PATH + "/{sampleId}";

    public final SampleService sampleService;

    @GetMapping(SAMPLE_PATH)
    public List<SampleDTO> listSamples() {
        return sampleService.listSamples();
    }

    @GetMapping(SAMPLE_PATH_ID)
    public SampleDTO getSampleById(@PathVariable("sampleId") UUID sampleId) {
        return sampleService.getSampleById(sampleId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(SAMPLE_PATH)
    public ResponseEntity<Void> createSample(@RequestBody SampleDTO sampleDTO) {
        SampleDTO savedSampleDTO = sampleService.saveNewSample(sampleDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/sample/" + savedSampleDTO.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(SAMPLE_PATH_ID)
    public ResponseEntity<Void> updateSampleById(@PathVariable("sampleId") UUID sampleId, @RequestBody SampleDTO sampleDTO) {
        sampleService.updateSampleById(sampleId, sampleDTO).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(SAMPLE_PATH_ID)
    public ResponseEntity<Void> deleteSampleById(@PathVariable("sampleId") UUID sampleId) {
        if (!sampleService.deleteSampleById(sampleId))
            throw new NotFoundException();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(SAMPLE_PATH_ID)
    public ResponseEntity<Void> patchSampleById(@PathVariable("sampleId") UUID sampleId, @RequestBody SampleDTO sampleDTO) {
        sampleService.patchSampleById(sampleId, sampleDTO).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}