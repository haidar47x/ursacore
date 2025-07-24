package com.ursacore.repository;

import com.ursacore.entity.TestCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestCategoryRepository extends JpaRepository<TestCategory, UUID> {}
