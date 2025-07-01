package com.ursacore.controller;


import com.ursacore.model.Sample;
import com.ursacore.service.SampleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RequestMapping("/api/v1/sample")
@RestController
@Slf4j
public class SampleController {

    public final SampleService sampleService;

    @GetMapping
    public List<Sample> listSamples() {
        return sampleService.listSamples();
    }

    @GetMapping("{sampleId}")
    public Sample getSampleById(@PathVariable("sampleId") UUID sampleId) {
        return sampleService.getSampleById(sampleId);
    }

    @PostMapping
    public ResponseEntity createSample(@RequestBody Sample sample) {
        Sample savedSample = sampleService.saveNewSample(sample);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/sample/" + savedSample.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("{sampleId}")
    public ResponseEntity updateSampleById(@PathVariable("sampleId") UUID sampleId, @RequestBody Sample sample) {
        sampleService.updateSampleById(sampleId, sample);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{sampleId}")
    public ResponseEntity deleteById(@PathVariable("sampleId") UUID sampleId) {
        sampleService.deleteById(sampleId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{sampleId}")
    public ResponseEntity patchSampleById(@PathVariable("sampleId") UUID sampleId, @RequestBody Sample sample) {
        sampleService.patchSampleById(sampleId, sample);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
