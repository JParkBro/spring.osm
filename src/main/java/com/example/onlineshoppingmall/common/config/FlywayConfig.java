package com.example.onlineshoppingmall.common.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Bean
    @DependsOn("dataSource")
    public Flyway flyway(@Qualifier("dataSource") DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .baselineVersion(MigrationVersion.fromVersion("0"))
                .validateOnMigrate(true)
                .load();
        flyway.migrate();
        return flyway;
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return Flyway::migrate;
    }
}
