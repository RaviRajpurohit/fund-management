package com.sapient.graphdb.neo4j;


import com.sapient.graphdb.HoldingsFinder;
import com.sapient.module.Holdings;
import com.sapient.module.Level;
import org.junit.jupiter.api.*;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.io.File;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

@DisplayName("Test scenario for holdings finder")
class HoldingsFinderTest {

    private HoldingsFinder holdingsFinder;
    private DatabaseManagementService managementService;
    private GraphDatabaseService service;

    @BeforeEach
    void initialize() {

        managementService = new DatabaseManagementServiceBuilder(new File("target/a.db")).build();
        service = managementService.database(DEFAULT_DATABASE_NAME);
        holdingsFinder = new HoldingsFinder();
        holdingsFinder.setGraphDB(service);
    }

    @AfterEach
    void cleanup() {
        try (Transaction transaction = service.beginTx()) {
            transaction.execute("MATCH (n) DETACH DELETE n");
            transaction.commit();
        }
        managementService.shutdown();
    }

    @Test
    @DisplayName("Should return holdings")
    void test() {
        try (Transaction transaction = service.beginTx()) {
            Node holdings = transaction.createNode(Label.label(Level.HOLDINGS.getName()));
            holdings.setProperty("name", "Milk");
            holdings.setProperty("value", 20.0);
            transaction.commit();
        }
        Holdings holdings = holdingsFinder.find("Milk", null);

        Assertions.assertNotNull(holdings);
        Assertions.assertEquals("Milk", holdings.getName());
        Assertions.assertEquals(20, holdings.getValue());
        try (Transaction transaction = service.beginTx()) {
            transaction.execute("match(n)detach delete n");
            transaction.commit();
        }
    }

    @Test
    @DisplayName("Should return null when holding not present")
    void testNotPresent() {
        try (Transaction transaction = service.beginTx()) {
            Node holdings = transaction.createNode(Label.label(Level.HOLDINGS.getName()));
            holdings.setProperty("name", "Milk");
            holdings.setProperty("value", 20.0);
            transaction.commit();
        }
        Holdings holdings = holdingsFinder.find("Curd", null);

        Assertions.assertNull(holdings);
        try (Transaction transaction = service.beginTx()) {
            transaction.execute("match(n)detach delete n");
            transaction.commit();
        }
    }
}