package com.sapient.graphdb.neo4j;

import com.sapient.QueryConstants;
import com.sapient.graphdb.FundNodeCreator;
import com.sapient.module.Level;
import org.junit.jupiter.api.*;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.graphdb.*;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;

@DisplayName("Test scenario for Fund node creator.")
class FundNodeCreatorTest {

    private GraphDatabaseService service;
    private DatabaseManagementService managementService;
    private FundNodeCreator nodeCreator;

    @BeforeEach
    void initialize() {
        managementService = new DatabaseManagementServiceBuilder(new File("target/a.db")).build();
        service = managementService.database(DEFAULT_DATABASE_NAME);
        nodeCreator = new FundNodeCreator();
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
    @DisplayName("Creator should create Fund with given property")
    void testCreateFundWithoutHoldings() {
        nodeCreator.create(Collections.singletonMap(QueryConstants.NAME, "Amul"), null, null);

        try (Transaction transaction = service.beginTx()) {
            Node node = transaction.findNode(Label.label(Level.FUND.getName()), QueryConstants.NAME, "Amul");
            Assertions.assertNotNull(node);
            Assertions.assertEquals("Amul", node.getProperty(QueryConstants.NAME));

            transaction.execute("MATCH (fund: Fund{name:'Amul'}) DELETE fund");
            transaction.commit();
        }
    }

    @Test
    @DisplayName("Creator should create Fund with given property where child node is empty map")
    void testCreateFundWithEmptyHoldings() {
        nodeCreator.create(Collections.singletonMap(QueryConstants.NAME, "Sapient"), Collections.EMPTY_MAP, null);

        try (Transaction transaction = service.beginTx()) {
            Node node = transaction.findNode(Label.label(Level.FUND.getName()), QueryConstants.NAME, "Sapient");
            Assertions.assertNotNull(node);
            Assertions.assertEquals("Sapient", node.getProperty(QueryConstants.NAME));

            transaction.execute("MATCH (fund: Fund{name:'Sapient'}) DELETE fund");
            transaction.commit();
        }
    }

    @Test
    @DisplayName("Creator should create Fund and Holdings with given property and relationship too")
    void testCreateFundWithHoldings() {
        Map<String, Object> childMap = new HashMap<>(2);
        childMap.put(QueryConstants.NAME, "Term Insurance");
        childMap.put(QueryConstants.VALUE, 20.0);

        nodeCreator.create(Collections.singletonMap(QueryConstants.NAME, "HDFC"),
                childMap,
                Collections.singletonMap(QueryConstants.QUANTITY, 4));

        try (Transaction transaction = service.beginTx()) {
            Node node = transaction.findNode(Label.label(Level.FUND.getName()), QueryConstants.NAME, "HDFC");
            Assertions.assertNotNull(node);
            Assertions.assertEquals("HDFC", node.getProperty(QueryConstants.NAME));
            Relationship relationship = node.getSingleRelationship(
                    RelationshipType.withName(QueryConstants.HOLD_RELATIONSHIP), Direction.OUTGOING);
            Assertions.assertNotNull(relationship);
            Assertions.assertEquals(4, relationship.getProperty(QueryConstants.QUANTITY));

            Node relatedNode = relationship.getEndNode();
            Assertions.assertNotNull(relatedNode);
            Assertions.assertEquals("Term Insurance", relatedNode.getProperty(QueryConstants.NAME));
            Assertions.assertEquals(20.0, relatedNode.getProperty(QueryConstants.VALUE));

            transaction.execute("MATCH (fund: Fund) DETACH DELETE fund");
            transaction.execute("MATCH (holdings: Holdings) DETACH DELETE holdings");
            transaction.commit();
        }
    }

    @Test
    @DisplayName("Creator should throw exception when try to create Fund and Holdings with given property and relationship too but edge attributes are empty")
    void testCreateFundWithHoldingsWithoutEdge() {
        Map<String, Object> childMap = new HashMap<>(2);
        childMap.put(QueryConstants.NAME, "Term Insurance");
        childMap.put(QueryConstants.VALUE, 20.0);

        Map<String, Object> node = Collections.singletonMap(QueryConstants.NAME, "HDFC");

        Assertions.assertThrows(IllegalArgumentException.class, () -> nodeCreator.create(node,
                childMap, null));

        Map<String, Object> edge = Collections.emptyMap();
        Assertions.assertThrows(IllegalArgumentException.class, () -> nodeCreator.create(node,
                childMap, edge));
    }
}
