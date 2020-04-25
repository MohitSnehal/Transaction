package com.transaction.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    private static final String PRIMARY_DATASOURCE_PREFIX = "spring.datasource";

    @Autowired
    private Environment environment;

    @Bean
    @Primary
    public DataSource dataSource() {
        final RoutingDataSource routingDataSource = new RoutingDataSource();

        final DataSource primaryDataSource = buildDataSource("PrimaryHikariPool", PRIMARY_DATASOURCE_PREFIX);

        final Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(RoutingDataSource.Route.PRIMARY, primaryDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(primaryDataSource);

        return routingDataSource;
    }

    private DataSource buildDataSource(String poolName, String dataSourcePrefix) {
        final HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setPoolName(poolName);
        hikariConfig.setJdbcUrl(environment.getProperty(String.format("%s.url", dataSourcePrefix)));
        hikariConfig.setUsername(environment.getProperty(String.format("%s.username", dataSourcePrefix)));
        hikariConfig.setPassword(environment.getProperty(String.format("%s.password", dataSourcePrefix)));

        return new HikariDataSource(hikariConfig);
    }
}
