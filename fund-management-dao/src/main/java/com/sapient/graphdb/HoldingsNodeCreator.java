package com.sapient.graphdb;

import com.sapient.QueryConstants;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class HoldingsNodeCreator implements NodeCreator {

    private static final Logger logger = LoggerFactory.getLogger(HoldingsNodeCreator.class);

    private GraphDatabaseService graphDB;

    @Autowired
    public void setGraphDB(GraphDatabaseService graphDB) {
        this.graphDB = graphDB;
    }

    @Override
    public void create(Map<String, Object> node, Map<String, Object> childNode, Map<String, Object> edgeProperties) {
        logger.info("Executing::HoldingsNodeCreator.create(holdingsNode: {})", node);

        try (Transaction transaction = graphDB.beginTx()) {
            logger.info("Executing Query for Holdings: {}", QueryConstants.QUERY_TO_CREATE_HOLDINGS);
            logger.trace("parameter: {}", node);
            transaction.execute(QueryConstants.QUERY_TO_CREATE_HOLDINGS, node);

            transaction.commit();
        }

        logger.info("Exiting::HoldingsNodeCreator.create()");
    }
}
