package com.ursacore.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientCsvRecord {

    @CsvBindByName(column = "Name")
    private String name;

    @CsvBindByName(column = "Age")
    private Integer age;

    @CsvBindByName(column = "Gender")
    private String gender;

    @CsvBindByName(column = "Blood Type")
    private String bloodType;

    @CsvBindByName(column = "Medical Condition")
    private String medicalCondition;

    @CsvBindByName(column = "Date of Admission")
    private String dateOfAdmission;

    @CsvBindByName(column = "Doctor")
    private String doctor;

    @CsvBindByName(column = "Hospital")
    private String hospital;

    @CsvBindByName(column = "Insurance Provider")
    private String insuranceProvider;

    @CsvBindByName(column = "Billing Amount")
    private Double billingAmount;

    @CsvBindByName(column = "Room Number")
    private String roomNumber;

    @CsvBindByName(column = "Admission Type")
    private String admissionType;

    @CsvBindByName(column = "Discharge Date")
    private String dischargeDate;

    @CsvBindByName(column = "Medication")
    private String medication;

    @CsvBindByName(column = "Test Results")
    private String testResults;

}
