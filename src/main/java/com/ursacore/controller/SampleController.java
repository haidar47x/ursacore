package com.ursacore.controller;

import com.ursacore.exceptions.NotFoundException;
import com.ursacore.model.Sample;
import com.ursacore.service.SampleService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Sample> listSamples() {
        return sampleService.listSamples();
    }

    @GetMapping(SAMPLE_PATH_ID)
    public Sample getSampleById(@PathVariable("sampleId") UUID sampleId) {
        return sampleService.getSampleById(sampleId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(SAMPLE_PATH)
    public ResponseEntity createSample(@RequestBody Sample sample) {
        Sample savedSample = sampleService.saveNewSample(sample);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/sample/" + savedSample.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(SAMPLE_PATH_ID)
    public ResponseEntity updateSampleById(@PathVariable("sampleId") UUID sampleId, @RequestBody Sample sample) {
        sampleService.updateSampleById(sampleId, sample);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(SAMPLE_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("sampleId") UUID sampleId) {
        sampleService.deleteById(sampleId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(SAMPLE_PATH_ID)
    public ResponseEntity patchSampleById(@PathVariable("sampleId") UUID sampleId, @RequestBody Sample sample) {
        sampleService.patchSampleById(sampleId, sample);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
