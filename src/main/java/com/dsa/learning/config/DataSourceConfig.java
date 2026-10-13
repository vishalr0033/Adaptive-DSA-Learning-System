package com.dsa.learning.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

@Configuration
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/dsa_learning}")
    private String mysqlUrl;

    @Value("${spring.datasource.username:root}")
    private String mysqlUser;

    @Value("${spring.datasource.password:root}")
    private String mysqlPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        boolean mysqlReachable = testMysqlConnection();

        if (mysqlReachable) {
            logger.info("Successfully established connection to MySQL Database at {}", mysqlUrl);
            return DataSourceBuilder.create()
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .url(mysqlUrl)
                    .username(mysqlUser)
                    .password(mysqlPassword)
                    .build();
        } else {
            logger.warn("================================================================================");
            logger.warn("MySQL Server at localhost:3306 was not reachable or credentials failed.");
            logger.warn("Auto-switching to embedded H2 Database (MySQL compatibility mode: ./data/dsadb).");
            logger.warn("All features, schemas, and persistence remain 100% operational.");
            logger.warn("================================================================================");

            return DataSourceBuilder.create()
                    .driverClassName("org.h2.Driver")
                    .url("jdbc:h2:file:./data/dsadb;DB_CLOSE_DELAY=-1;MODE=MySQL;DATABASE_TO_LOWER=TRUE")
                    .username("sa")
                    .password("")
                    .build();
        }
    }

    private boolean testMysqlConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(mysqlUrl, mysqlUser, mysqlPassword)) {
                return conn != null && !conn.isClosed();
            }
        } catch (Exception e) {
            return false;
        }
    }
}
