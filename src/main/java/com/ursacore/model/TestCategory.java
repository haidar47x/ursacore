package com.ursacore.model;

/** It should list the medical tests for patients like endicronology, cardiology, etc. */
public enum TestCategory {

    ENDOCRINOLOGY("Endocrinology"),
    CARDIOLOGY("Cardiology"),
    NEUROLOGY("Neurology"),
    GASTROENTEROLOGY("Gastroenterology"),
    HEMATOLOGY("Hematology"),
    ONCOLOGY("Oncology"),
    IMMUNOLOGY("Immunology"),
    INFECTIOUS_DISEASES("Infectious Diseases"),
    PATHALOGY("Pathology"),
    MICROBIOLOGY("Microbiology"),
    TOXICOLOGY("Toxicology"),
    GENETICS("Genetics"),
    RHEUMATOLOGY("Rheumatology"),
    PSYCHIATRY("Psychiatry"),
    PULMONOLOGY("Pulmonology"),
    DERMATOLOGY("Dermatology"),
    UROLOGY("Urology"),
    ORTHOPEDICS("Orthopedics"),
    GYNECOLOGY("Gynecology"),
    PEDIATRICS("Pediatrics"),
    OPHTHALMOLOGY("Ophthalmology"),
    OTOLARYNGOLOGY("Otolaryngology"),
    NEPHROLOGY("Nephrology"),
    DENTISTRY("Dentistry"),
    GERIATRICS("Geriatrics"),
    OTHER("Other");

    private final String displayName;

    TestCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
