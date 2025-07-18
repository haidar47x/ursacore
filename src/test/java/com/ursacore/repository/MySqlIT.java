package com.ursacore.repository;

import com.ursacore.entity.Sample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("localmysql")
public class MySqlIT {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySqlContainer = new MySQLContainer<>("mysql:8");

    @Autowired
    SampleRepository sampleRepository;

    @Test
    void testListSamples() {
        List<Sample> samples = sampleRepository.findAll();
        assertThat(samples.size()).isEqualTo(2503);
    }
}
