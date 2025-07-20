package com.ursacore.repository;

import com.ursacore.entity.Patient;
import com.ursacore.model.BloodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findAllByNameIsLikeIgnoreCase(String name);

    List<Patient> findAllByBloodType(BloodType type);

    List<Patient> findAllByNameIsLikeIgnoreCaseAndBloodType(String name, BloodType type);
}
