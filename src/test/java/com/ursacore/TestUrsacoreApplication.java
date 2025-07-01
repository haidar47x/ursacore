package com.ursacore;

import org.springframework.boot.SpringApplication;

public class TestUrsacoreApplication {

    public static void main(String[] args) {
        SpringApplication.from(UrsacoreApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
