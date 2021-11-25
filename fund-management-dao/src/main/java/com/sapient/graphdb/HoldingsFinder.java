package com.sapient.graphdb;

import com.sapient.QueryConstants;
import com.sapient.module.Holdings;
import com.sapient.module.Level;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class HoldingsFinder implements Finder<Holdings> {
    private static final Logger logger = LoggerFactory.getLogger(HoldingsFinder.class);

    private GraphDatabaseService graphDB;

    @Autowired
    public void setGraphDB(GraphDatabaseService graphDB) {
        this.graphDB = graphDB;
    }

    @Override
    public Holdings find(String name, Set<String> holdingsToExclusion) {
        logger.debug("Executing::HoldingsFinder.find(name: {})", name);
        try (Transaction transaction = graphDB.beginTx()) {

            Node node = transaction.findNode(Label.label(Level.HOLDINGS.getName()), QueryConstants.NAME, name);
            return QueryConstants.getHoldingsFromNode(node);
        } finally {
            logger.debug("Exiting::HoldingsFinder.find()");
        }
    }
}
