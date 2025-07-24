package com.ursacore.repository;

import com.ursacore.entity.Patient;
import com.ursacore.entity.Sample;
import com.ursacore.entity.TestOrder;
import com.ursacore.model.TestType;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

// Uncomment to use a MySQL container for testing
// @Active Profiles("localmysql")
@Slf4j
@SpringBootTest
class TestOrderRepositoryTest {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    SampleRepository sampleRepository;

    @Autowired
    TestOrderRepository testOrderRepository;

    Patient patient;
    Sample sample;

    @BeforeEach
    void setUp() {
        patient = patientRepository.findAll().getFirst();
        sample = sampleRepository.findAll().getFirst();
    }

    @Transactional
    @Test
    void testTestOrderRepositoryExists() {
        var testOrder = TestOrder.builder()
                    .testType(TestType.BLOOD)
                    .patientRef("a patient reference")
                    .patient(patient)
                .build();

        var savedOrder = testOrderRepository.save(testOrder);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getPatient()).isNotNull();
        assertThat(savedOrder.getPatient().getId()).isEqualTo(patient.getId());
        assertThat(savedOrder.getPatient().getTestOrders()).isNotNull();
        assertThat(savedOrder.getPatient().getTestOrders().size()).isNotZero();
        assertThat(savedOrder.getPatientRef()).isEqualTo(testOrder.getPatientRef());
    }

    @Rollback
    @Transactional
    @Test
    void testCreateTestOrderManualRelation() {
        var testOrder = TestOrder.builder()
                    .testType(TestType.BLOOD)
                    .patientRef("a patient reference")
                    .patient(patient)
                .build();

        // By default, the testOrders in patient will be empty because Hibernate
        // defaults to AUTO flush mode which flushes the data to the database from
        // the persistence context when it needs to. Therefore, we trigger the flush using
        // saveAndFlush(...), which ensures that the pending data is forced flushed to the database.
        // On the contrary, if we use save(...), the data will not be flushed to the database,
        // and we'll get 0 entries in savedOrder.patient.testOrders
        var savedOrder = testOrderRepository.saveAndFlush(testOrder);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getPatient()).isNotNull();
        assertThat(savedOrder.getPatient().getId()).isEqualTo(patient.getId());

        // If there's implicit transaction, the orders will throw LazyInitializationException
        // Therefore, we annotate the test with @Transactional
        assertThat(savedOrder.getPatient().getTestOrders()).isNotNull();

        // By default, the patient should have no orders if we don't create any
        // for the patient after the order creation. However, we're using saveAndFlush(...)
        // that triggers force flush to the database and the testOrders should be populated
        // in this instance.
        assertThat(savedOrder.getPatient().getTestOrders().size()).isNotZero();
        assertThat(savedOrder.getPatientRef()).isEqualTo(testOrder.getPatientRef());
    }



    @Rollback
    @Transactional
    @Test
    void testCreateTestOrderViaFlush() {
        var testOrder = TestOrder.builder()
                    .testType(TestType.BLOOD)
                    .patientRef("a patient reference")
                    .patient(patient)
                .build();

        // By default, the testOrders in patient will be empty because Hibernate
        // defaults to AUTO flush mode which flushes the data to the database from
        // the persistence context when it needs to. Therefore, we trigger the flush using
        // saveAndFlush(...), which ensures that the pending data is forced flushed to the database.
        // On the contrary, if we use save(...), the data will not be flushed to the database,
        // and we'll get 0 entries in savedOrder.patient.testOrders
        var savedOrder = testOrderRepository.saveAndFlush(testOrder);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getPatient()).isNotNull();
        assertThat(savedOrder.getPatient().getId()).isEqualTo(patient.getId());

        // If there's implicit transaction, the orders will throw LazyInitializationException
        // Therefore, we annotate the test with @Transactional
        assertThat(savedOrder.getPatient().getTestOrders()).isNotNull();

        // By default, the patient should have no orders if we don't create any
        // for the patient after the order creation. However, we're using saveAndFlush(...)
        // that triggers force flush to the database and the testOrders should be populated
        // in this instance.
        assertThat(savedOrder.getPatient().getTestOrders().size()).isNotZero();
        assertThat(savedOrder.getPatientRef()).isEqualTo(testOrder.getPatientRef());
    }

}