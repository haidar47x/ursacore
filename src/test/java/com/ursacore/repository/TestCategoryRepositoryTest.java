package com.ursacore.repository;

import com.ursacore.entity.Patient;
import com.ursacore.entity.TestCategory;
import com.ursacore.entity.TestOrder;
import com.ursacore.model.SampleType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestCategoryRepositoryTest {

    @Autowired
    TestCategoryRepository testCategoryRepository;

    @Autowired
    TestCategoryRepository testOrderRepository;

    @Autowired
    PatientRepository patientRepository;

    Patient patient;
    TestOrder testOrder;

    @BeforeEach
    void setUp() {
        patient = patientRepository.findAll().getFirst();
        testOrder = TestOrder.builder()
                .sampleType(SampleType.BLOOD)
                .patient(patient)
                .patientRef("a patient reference")
            .build();

    }

    @Transactional
    @Test
    void testCreateNewCategory() {
        var testCategory = TestCategory.builder()
                .categoryName("TSH")
                .description("Thyroid Stimulating Hormone test for thyroid function")
                .type(SampleType.BLOOD)
            .build();

        testCategory.getTestOrders().add(testOrder);
        testOrder.getTestCategories().add(testCategory);
        var savedTestCategory = testCategoryRepository.save(testCategory);

        assertThat(savedTestCategory).isNotNull();
        assertThat(savedTestCategory.getTestOrders()).isNotNull();
        assertThat(savedTestCategory.getTestOrders().size()).isNotZero();
    }
}