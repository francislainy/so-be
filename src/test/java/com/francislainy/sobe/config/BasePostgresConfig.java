package com.francislainy.sobe.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@Testcontainers
public class BasePostgresConfig {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgres;

    static {
        postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres").withTag("latest"));
        postgres.start();

        // To enable TestContainers https://plugins.jetbrains.com/plugin/17116-testcontainers-port-updater (match everything)
        // Postgres DB url and credentials can also be printed and then added to IntelliJ datasource configurations
        log.info("Database: {}", postgres.getJdbcUrl());
    }
}
