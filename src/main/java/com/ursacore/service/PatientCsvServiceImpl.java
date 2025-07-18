package com.ursacore.service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.ursacore.model.PatientCsvRecord;
import com.ursacore.model.PatientDTO;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.List;

@Service
public class PatientCsvServiceImpl implements PatientCsvService {

    @Override
    public List<PatientCsvRecord> convertCsv(File csvFile) {
        try {
            return new CsvToBeanBuilder<PatientCsvRecord>(new FileReader(csvFile))
                    .withType(PatientCsvRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
        } catch (Exception e) {
            throw new RuntimeException("Error processing CSV file: " + csvFile.getName(), e);
        }
    }
}
