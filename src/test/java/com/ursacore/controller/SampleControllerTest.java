package com.ursacore.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SampleControllerTest {

    @Autowired
    SampleController sampleController;

    @Test
    void getSampleById() {

        System.out.println(sampleController.getSampleById(UUID.randomUUID()));
    }
}