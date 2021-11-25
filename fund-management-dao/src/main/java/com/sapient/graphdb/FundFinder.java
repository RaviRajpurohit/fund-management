package com.sapient.graphdb;

import com.sapient.QueryConstants;
import com.sapient.module.Fund;
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
public class FundFinder implements Finder<Fund> {
    private static final Logger logger = LoggerFactory.getLogger(FundFinder.class);

    private GraphDatabaseService graphDB;

    @Autowired
    public void setGraphDB(GraphDatabaseService graphDB) {
        this.graphDB = graphDB;
    }

    @Override
    public Fund find(String name, Set<String> holdingsToExclusion) {
        logger.debug("Executing::FundFinder.find(name: {}, holdingsToExclusion: {})", name, holdingsToExclusion);
        try (Transaction transaction = graphDB.beginTx()) {

            Node node = transaction.findNode(Label.label(Level.FUND.getName()), QueryConstants.NAME, name);
            return QueryConstants.getFundFromNode(node, holdingsToExclusion);
        } finally {
            logger.debug("Exiting::FundFinder.find()");
        }
    }
}
