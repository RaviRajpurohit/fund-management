package com.sapient.graphdb;

import com.sapient.QueryConstants;
import com.sapient.module.Level;
import org.neo4j.graphdb.*;
import org.neo4j.kernel.impl.core.NodeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class FundNodeCreator implements NodeCreator {

    private static final Logger logger = LoggerFactory.getLogger(FundNodeCreator.class);

    private GraphDatabaseService graphDB;

    @Autowired
    public void setGraphDB(GraphDatabaseService graphDB) {
        this.graphDB = graphDB;
    }

    @Override
    public void create(Map<String, Object> node, Map<String, Object> childNode, Map<String, Object> edgeProperties) {
        logger.info("Executing::FundNodeCreator.create(fundNode: {}, holdingNode: {}, edge: {})", node, childNode, edgeProperties);

        try (Transaction transaction = graphDB.beginTx()) {
            logger.info("Executing Query for Fund: {}", QueryConstants.QUERY_TO_CREATE_FUND);
            logger.trace("parameter: {}", node);
            Node fund = (NodeEntity) transaction.execute(QueryConstants.QUERY_TO_CREATE_FUND, node)
                    .next().get(Level.FUND.getName().toLowerCase());

            if (childNode != null && !childNode.isEmpty()) {
                logger.info("Executing Query for Holding: {}", QueryConstants.QUERY_TO_CREATE_HOLDINGS);
                logger.trace("parameter: {}", childNode);
                Node holding = (NodeEntity) transaction.execute(QueryConstants.QUERY_TO_CREATE_HOLDINGS, childNode)
                        .next().get(Level.HOLDINGS.getName().toLowerCase());

                if (edgeProperties == null || !edgeProperties.containsKey(QueryConstants.QUANTITY)) {
                    transaction.rollback();
                    throw new IllegalArgumentException("Required parameter is missing, to create relationship between FUND and HOLDINGS quantity is mandatory.");
                }

                logger.info("creating [:HOLD] relationship between fund and holdings with edge: {}", edgeProperties);
                Relationship relationship = fund.createRelationshipTo(holding, RelationshipType.withName(QueryConstants.HOLD_RELATIONSHIP));
                relationship.setProperty(QueryConstants.QUANTITY, edgeProperties.get(QueryConstants.QUANTITY));
            }

            transaction.commit();
        }

        logger.info("Exiting::FundNodeCreator.create()");
    }
}
