package com.ursacore.service;

import com.ursacore.model.PatientCsvRecord;

import java.io.File;
import java.util.List;

public interface PatientCsvService {

    List<PatientCsvRecord> convertCsv(File csvFile);

}
