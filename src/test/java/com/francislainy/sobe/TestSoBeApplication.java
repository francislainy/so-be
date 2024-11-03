package com.francislainy.sobe;

import org.springframework.boot.SpringApplication;

public class TestSoBeApplication {

    public static void main(String[] args) {
        SpringApplication.from(SoBeApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
