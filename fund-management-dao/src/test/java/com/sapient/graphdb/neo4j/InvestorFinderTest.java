package com.sapient.graphdb.neo4j;

import com.sapient.graphdb.InvestorFinder;
import com.sapient.module.Fund;
import com.sapient.module.Holdings;
import com.sapient.module.Investor;
import com.sapient.module.Level;
import org.junit.jupiter.api.*;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.*;

import java.io.File;
import java.util.Collections;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

@DisplayName("Test scenario for investor finder")
class InvestorFinderTest {

    private InvestorFinder investorFinder;
    private DatabaseManagementService managementService;
    private GraphDatabaseService service;

    @BeforeEach
    void initialize() {
        managementService = new DatabaseManagementServiceBuilder(new File("target/a.db")).build();
        service = managementService.database(DEFAULT_DATABASE_NAME);
        investorFinder = new InvestorFinder();
        investorFinder.setGraphDB(service);
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
    @DisplayName("Should return investor with all holdings")
    void test() {
        try (Transaction transaction = service.beginTx()) {
            Node node = transaction.createNode(Label.label(Level.INVESTOR.getName()));
            node.setProperty("name", "Radha");

            Node childNode = transaction.createNode(Label.label(Level.FUND.getName()));
            childNode.setProperty("name", "Gokul");

            node.createRelationshipTo(childNode, RelationshipType.withName("HAS"));

            Node holdings = transaction.createNode(Label.label(Level.HOLDINGS.getName()));
            holdings.setProperty("name", "Milk");
            holdings.setProperty("value", 20);

            Relationship relationship = childNode.createRelationshipTo(holdings, RelationshipType.withName("HOLD"));
            relationship.setProperty("quantity", 10);
            transaction.commit();
        }
        Investor investor = investorFinder.find("Radha", Collections.emptySet());

        Assertions.assertNotNull(investor);
        Assertions.assertEquals("Radha", investor.getName());

        Assertions.assertEquals(1, investor.getFunds().size());

        Fund fund = investor.getFunds().iterator().next();
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
    @DisplayName("Should return investor without holdings those are given for exclusion")
    void testExclusion() {
        try (Transaction transaction = service.beginTx()) {
            Node node = transaction.createNode(Label.label(Level.INVESTOR.getName()));
            node.setProperty("name", "Radha");

            Node childNode = transaction.createNode(Label.label(Level.FUND.getName()));
            childNode.setProperty("name", "Gokul");

            node.createRelationshipTo(childNode, RelationshipType.withName("HAS"));

            Node holdings = transaction.createNode(Label.label(Level.HOLDINGS.getName()));
            holdings.setProperty("name", "Milk");
            holdings.setProperty("value", 20);

            Relationship relationship = childNode.createRelationshipTo(holdings, RelationshipType.withName("HOLD"));
            relationship.setProperty("quantity", 10);
            transaction.commit();
        }
        Investor investor = investorFinder.find("Radha", Collections.singleton("Milk"));

        Assertions.assertNotNull(investor);
        Assertions.assertEquals("Radha", investor.getName());

        Assertions.assertEquals(1, investor.getFunds().size());

        Fund fund = investor.getFunds().iterator().next();
        Assertions.assertNotNull(fund);
        Assertions.assertEquals("Gokul", fund.getName());

        Assertions.assertEquals(0, fund.getHoldingsWithQuantity().size());

        try (Transaction transaction = service.beginTx()) {
            transaction.execute("match(n)detach delete n");
            transaction.commit();
        }
    }

    @Test
    @DisplayName("Should return null when investor not present")
    void testNotPresent() {
        try (Transaction transaction = service.beginTx()) {
            Node node = transaction.createNode(Label.label(Level.INVESTOR.getName()));
            node.setProperty("name", "Radha");

            Node childNode = transaction.createNode(Label.label(Level.FUND.getName()));
            childNode.setProperty("name", "Gokul");

            node.createRelationshipTo(childNode, RelationshipType.withName("HAS"));

            Node holdings = transaction.createNode(Label.label(Level.HOLDINGS.getName()));
            holdings.setProperty("name", "Milk");
            holdings.setProperty("value", 20);

            Relationship relationship = childNode.createRelationshipTo(holdings, RelationshipType.withName("HOLD"));
            relationship.setProperty("quantity", 10);
            transaction.commit();
        }
        Investor investor = investorFinder.find("Krishna", Collections.singleton("Milk"));

        Assertions.assertNull(investor);

        try (Transaction transaction = service.beginTx()) {
            transaction.execute("match(n)detach delete n");
            transaction.commit();
        }
    }
}
