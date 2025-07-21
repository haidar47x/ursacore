package com.ursacore.repository;

import com.ursacore.entity.Patient;
import com.ursacore.model.BloodType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Page<Patient> findAllByNameIsLikeIgnoreCase(String name, Pageable pageable);

    Page<Patient> findAllByBloodType(BloodType type, Pageable pageable);

    Page<Patient> findAllByNameIsLikeIgnoreCaseAndBloodType(String name, BloodType type, Pageable pageable);
}
