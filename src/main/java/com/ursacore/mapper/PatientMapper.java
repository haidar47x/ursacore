package com.ursacore.mapper;

import com.ursacore.entity.Patient;
import com.ursacore.model.PatientDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PatientMapper {

    Patient patientDtoToPatient(PatientDTO dto);

    PatientDTO patientToPatientDto(Patient patient);
}
