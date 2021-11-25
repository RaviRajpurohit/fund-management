package com.sapient;

import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.sapient")
public class FundManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundManagementApplication.class, args);
    }

    @Bean
    public GraphDatabaseService graphDB() throws IOException {
        DatabaseManagementService managementService = new DatabaseManagementServiceBuilder(new File("fund-management-ws/target/graph.db")).build();
        return managementService.database(DEFAULT_DATABASE_NAME);
    }

}
