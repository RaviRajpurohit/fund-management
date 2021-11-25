package com.sapient.graphdb.neo4j;

import com.sapient.QueryConstants;
import com.sapient.graphdb.HoldingsNodeCreator;
import com.sapient.module.Level;
import org.junit.jupiter.api.*;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

@DisplayName("Test scenario for Holdings node creator.")
class HoldingsNodeCreatorTest {

    private GraphDatabaseService service;
    private DatabaseManagementService managementService;
    private HoldingsNodeCreator nodeCreator;

    @BeforeEach
    void initialize() {
        managementService = new DatabaseManagementServiceBuilder(new File("target/a.db")).build();
        service = managementService.database(DEFAULT_DATABASE_NAME);
        nodeCreator = new HoldingsNodeCreator();
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
    @DisplayName("Creator should create holdings node with given properties")
    void createNode() {
        Map<String, Object> node = new HashMap<>(2);
        node.put(QueryConstants.NAME, "Life Insurance");
        node.put(QueryConstants.VALUE, 80.55);
        nodeCreator.create(node, null, null);

        try (Transaction transaction = service.beginTx()) {
            Node holdings = transaction.findNode(Label.label(Level.HOLDINGS.getName()), QueryConstants.NAME, "Life Insurance");
            Assertions.assertNotNull(holdings);
            Assertions.assertEquals("Life Insurance", holdings.getProperty(QueryConstants.NAME));
            Assertions.assertEquals(80.55, holdings.getProperty(QueryConstants.VALUE));
        }
    }
}