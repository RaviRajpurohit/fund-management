package com.sapient.graphdb.neo4j;

import com.sapient.QueryConstants;
import com.sapient.graphdb.InvestorNodeCreator;
import com.sapient.module.Level;
import org.junit.jupiter.api.*;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.*;

import java.io.File;
import java.util.Collections;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

@DisplayName("Test scenario for investor node creator.")
class InvestorNodeCreatorTest {

    private GraphDatabaseService service;
    private DatabaseManagementService managementService;
    private InvestorNodeCreator nodeCreator;

    @BeforeEach
    void initialize() {
        managementService = new DatabaseManagementServiceBuilder(new File("target/a.db")).build();
        service = managementService.database(DEFAULT_DATABASE_NAME);
        nodeCreator = new InvestorNodeCreator();
        nodeCreator.setGraphDB(service);
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
    @DisplayName("Creator should create Investor with given property")
    void testCreateInvestorWithoutFund() {
        nodeCreator.create(Collections.singletonMap(QueryConstants.NAME, "Radha Rani"), null, null);

        try (Transaction transaction = service.beginTx()) {
            Node node = transaction.findNode(Label.label(Level.INVESTOR.getName()), QueryConstants.NAME, "Radha Rani");
            Assertions.assertNotNull(node);
            Assertions.assertEquals("Radha Rani", node.getProperty(QueryConstants.NAME));

            transaction.execute("MATCH (in: Investor{name:'Radha Rani'}) DELETE in");
            transaction.commit();
        }
    }

    @Test
    @DisplayName("Creator should create Investor with given property where child node is empty map")
    void testCreateInvestorWithEmptyFund() {
        nodeCreator.create(Collections.singletonMap(QueryConstants.NAME, "Govind"), Collections.emptyMap(), null);

        try (Transaction transaction = service.beginTx()) {
            Node node = transaction.findNode(Label.label(Level.INVESTOR.getName()), QueryConstants.NAME, "Govind");
            Assertions.assertNotNull(node);
            Assertions.assertEquals("Govind", node.getProperty(QueryConstants.NAME));

            transaction.execute("MATCH (in: Investor{name:'Govind'}) DELETE in");
            transaction.commit();
        }
    }

    @Test
    @DisplayName("Creator should create Investor and Fund with given property and relationship too")
    void testCreateInvestorWithFund() {
        nodeCreator.create(Collections.singletonMap(QueryConstants.NAME, "Krishna"),
                Collections.singletonMap(QueryConstants.NAME, "Amul"), null);

        try (Transaction transaction = service.beginTx()) {
            Node node = transaction.findNode(Label.label(Level.INVESTOR.getName()), QueryConstants.NAME, "Krishna");
            Assertions.assertNotNull(node);
            Assertions.assertEquals("Krishna", node.getProperty(QueryConstants.NAME));
            Relationship relationship = node.getSingleRelationship(
                    RelationshipType.withName(QueryConstants.HAS_RELATIONSHIP), Direction.OUTGOING);
            Assertions.assertNotNull(relationship);
            Node relatedNode = relationship.getEndNode();
            Assertions.assertNotNull(relatedNode);
            Assertions.assertEquals("Amul", relatedNode.getProperty(QueryConstants.NAME));

            transaction.execute("MATCH (in: Investor) DETACH DELETE in");
            transaction.execute("MATCH (fund: Fund) DETACH DELETE fund");
            transaction.commit();
        }
    }

}

