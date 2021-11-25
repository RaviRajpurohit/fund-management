package com.sapient.graphdb.neo4j;

import com.sapient.graphdb.FundFinder;
import com.sapient.module.Fund;
import com.sapient.module.Holdings;
import com.sapient.module.Level;
import org.junit.jupiter.api.*;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.*;

import java.io.File;
import java.util.Collections;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

@DisplayName("Test scenario for fund finder")
class FundFinderTest {

    private FundFinder fundFinder;
    private DatabaseManagementService managementService;
    private GraphDatabaseService service;

    @BeforeEach
    void initialize() {

        managementService = new DatabaseManagementServiceBuilder(new File("target/a.db")).build();
        service = managementService.database(DEFAULT_DATABASE_NAME);
        fundFinder = new FundFinder();
        fundFinder.setGraphDB(service);
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
    @DisplayName("Should return fund with all holdings")
    void test() {
        try (Transaction transaction = service.beginTx()) {
            Node childNode = transaction.createNode(Label.label(Level.FUND.getName()));
            childNode.setProperty("name", "Gokul");

            Node holdings = transaction.createNode(Label.label(Level.HOLDINGS.getName()));
            holdings.setProperty("name", "Milk");
            holdings.setProperty("value", 20);

            Relationship relationship = childNode.createRelationshipTo(holdings, RelationshipType.withName("HOLD"));
            relationship.setProperty("quantity", 10);
            transaction.commit();
        }
        Fund fund = fundFinder.find("Gokul", null);

        Assertions.assertNotNull(fund);
        Assertions.assertEquals("Gokul", fund.getName());

        Holdings holdings = fund.getHoldingsWithQuantity().keySet().iterator().next();
        Assertions.assertNotNull(holdings);
        Assertions.assertEquals("Milk", holdings.getName());
        Assertions.assertEquals(20, holdings.getValue());
        Assertions.assertEquals(10, fund.getHoldingsWithQuantity().get(holdings));
        try (Transaction transaction = service.beginTx()) {
            transaction.execute("match(n)detach delete n");
            transaction.commit();
        }
    }

    @Test
    @DisplayName("Should return null when fund not present")
    void testNotPresent() {
        try (Transaction transaction = service.beginTx()) {
            Node childNode = transaction.createNode(Label.label(Level.FUND.getName()));
            childNode.setProperty("name", "Gokul");

            Node holdings = transaction.createNode(Label.label(Level.HOLDINGS.getName()));
            holdings.setProperty("name", "Milk");
            holdings.setProperty("value", 20);

            Relationship relationship = childNode.createRelationshipTo(holdings, RelationshipType.withName("HOLD"));
            relationship.setProperty("quantity", 10);
            transaction.commit();
        }
        Fund fund = fundFinder.find("Mathura", null);

        Assertions.assertNull(fund);

        try (Transaction transaction = service.beginTx()) {
            transaction.execute("match(n)detach delete n");
            transaction.commit();
        }
    }

    @Test
    @DisplayName("Should return fund without holdings those are given for exclusion")
    void testExclusion() {
        try (Transaction transaction = service.beginTx()) {
            Node childNode = transaction.createNode(Label.label(Level.FUND.getName()));
            childNode.setProperty("name", "Gokul");

            Node holdings = transaction.createNode(Label.label(Level.HOLDINGS.getName()));
            holdings.setProperty("name", "Milk");
            holdings.setProperty("value", 20);

            Relationship relationship = childNode.createRelationshipTo(holdings, RelationshipType.withName("HOLD"));
            relationship.setProperty("quantity", 10);
            transaction.commit();
        }
        Fund fund = fundFinder.find("Gokul", Collections.singleton("Milk"));

        Assertions.assertNotNull(fund);
        Assertions.assertEquals("Gokul", fund.getName());

        Assertions.assertEquals(0, fund.getHoldingsWithQuantity().size());

        try (Transaction transaction = service.beginTx()) {
            transaction.execute("match(n)detach delete n");
            transaction.commit();
        }
    }
}
