package com.sapient.graphdb;

import com.sapient.QueryConstants;
import com.sapient.module.Level;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.core.NodeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class InvestorNodeCreator implements NodeCreator {

    private static final Logger logger = LoggerFactory.getLogger(InvestorNodeCreator.class);

    private GraphDatabaseService graphDB;

    @Autowired
    public void setGraphDB(GraphDatabaseService graphDB) {
        this.graphDB = graphDB;
    }

    @Override
    public void create(Map<String, Object> node, Map<String, Object> childNode, Map<String, Object> edgeProperties) {
        logger.info("Executing::InvestorNodeCreator.create(investorNode: {}, fundNode: {})", node, childNode);

        try (Transaction transaction = graphDB.beginTx()) {
            logger.info("Executing Query for investor: {}", QueryConstants.QUERY_TO_CREATE_INVESTOR);
            logger.trace("parameter: {}", node);
            Node investor = (NodeEntity) transaction.execute(QueryConstants.QUERY_TO_CREATE_INVESTOR, node)
                    .next().get(Level.INVESTOR.getName().toLowerCase());

            if (childNode != null && !childNode.isEmpty()) {
                logger.info("Executing Query for Fund: {}", QueryConstants.QUERY_TO_CREATE_FUND);
                logger.trace("parameter: {}", childNode);
                Node fund = (NodeEntity) transaction.execute(QueryConstants.QUERY_TO_CREATE_FUND, childNode)
                        .next().get(Level.FUND.getName().toLowerCase());

                logger.info("creating [:HAS] relationship between investor and fund");
                investor.createRelationshipTo(fund, RelationshipType.withName(QueryConstants.HAS_RELATIONSHIP));
            }

            transaction.commit();
        }

        logger.info("Exiting::InvestorNodeCreator.create()");
    }
}
