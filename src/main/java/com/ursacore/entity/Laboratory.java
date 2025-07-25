package com.ursacore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.validator.constraints.URL;

@Entity
public class Laboratory {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private String id;

    @NotNull
    @Column(length = 256, unique = true, nullable = false)
    private String name;

    @Column(length = 1024)
    private String description;

    @NotNull
    @Column(length = 512, unique = true, nullable = false)
    private String address;

    @NotNull
    @Column(length = 64, unique = true, nullable = false)
    private String phoneNumber;

    @Email
    private String email;

    @URL
    private String website;

    private String socialMediaHandle;
}
