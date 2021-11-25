package com.sapient;

import com.sapient.module.Fund;
import com.sapient.module.Holdings;
import com.sapient.module.Investor;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import java.util.*;

public class QueryConstants {

    public static final String HAS_RELATIONSHIP = "HAS";
    public static final String NAME = "name";
    public static final String QUANTITY = "quantity";
    public static final String HOLD_RELATIONSHIP = "HOLD";
    public static final String VALUE = "value";

    public static final String QUERY_TO_CREATE_INVESTOR = "MERGE (investor: Investor {name:$name}) ON CREATE SET investor.name=$name, investor.created = datetime() RETURN investor";
    public static final String QUERY_TO_CREATE_FUND = "MERGE (fund: Fund {name:$name}) ON CREATE SET fund.name=$name, fund.created = datetime() RETURN fund";
    public static final String QUERY_TO_CREATE_HOLDINGS = "MERGE (holdings: Holdings {name:$name}) ON CREATE SET holdings.name=$name, holdings.value=$value, holdings.created = datetime() ON MATCH SET holdings.value=$value, holdings.updated=datetime() RETURN holdings";

    private QueryConstants() {
    }

    public static Investor getInvestorFromNode(Node investorNode, Set<String> holdingsToExclusion) {
        if (investorNode == null) {
            return null;
        }
        Investor investor = new Investor();
        investor.setName(String.valueOf(investorNode.getProperty(NAME)));

        Iterator<Relationship> relationships = investorNode.getRelationships(
                RelationshipType.withName(QueryConstants.HAS_RELATIONSHIP)).iterator();

        Set<Fund> funds = new HashSet<>();
        while (relationships.hasNext()) {
            funds.add(getFundFromNode(relationships.next().getEndNode(), holdingsToExclusion));
        }
        investor.setFunds(funds);
        return investor;
    }

    public static Fund getFundFromNode(Node fundNode, Set<String> holdingsToExclusion) {
        if (fundNode == null) {
            return null;
        }
        Fund fund = new Fund();
        fund.setName(String.valueOf(fundNode.getProperty(QueryConstants.NAME)));

        Iterator<Relationship> relationships = fundNode.getRelationships(
                RelationshipType.withName(HOLD_RELATIONSHIP)).iterator();

        Map<Holdings, Integer> holdings = new HashMap<>();
        while (relationships.hasNext()) {
            Relationship relationship = relationships.next();

            Node holdingNode = relationship.getEndNode();

            if (holdingsToExclusion == null
                    || !holdingsToExclusion.contains(String.valueOf(holdingNode.getProperty(NAME)))) {
                holdings.put(getHoldingsFromNode(holdingNode), (int) relationship.getProperty(QUANTITY));
            }
        }
        fund.setHoldingsWithQuantity(holdings);
        return fund;
    }

    public static Holdings getHoldingsFromNode(Node holdingNode) {
        if (holdingNode == null) {
            return null;
        }
        Holdings holdings = new Holdings();
        holdings.setName(String.valueOf(holdingNode.getProperty(NAME)));
        holdings.setValue(Double.valueOf(holdingNode.getProperty(VALUE).toString()));
        return holdings;
    }

}

