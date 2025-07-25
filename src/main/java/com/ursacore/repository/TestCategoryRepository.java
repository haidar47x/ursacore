package com.ursacore.repository;

import com.ursacore.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestCategoryRepository extends JpaRepository<LabTest, UUID> {}
