package com.sapient.service.impl.test;

import com.sapient.graphdb.FundNodeCreator;
import com.sapient.graphdb.HoldingsNodeCreator;
import com.sapient.graphdb.InvestorNodeCreator;
import com.sapient.module.Level;
import com.sapient.service.impl.NodeCreatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("Unit test for factory class")
class NodeCreatorFactoryTest {

    private static NodeCreatorFactory nodeCreatorFactory;
    private static InvestorNodeCreator investorNodeCreator;
    private static FundNodeCreator fundNodeCreator;
    private static HoldingsNodeCreator holdingsNodeCreator;

    @BeforeAll
    static void initialize() {
        investorNodeCreator = Mockito.mock(InvestorNodeCreator.class);
        fundNodeCreator = Mockito.mock(FundNodeCreator.class);
        holdingsNodeCreator = Mockito.mock(HoldingsNodeCreator.class);

        nodeCreatorFactory = new NodeCreatorFactory();
        nodeCreatorFactory.setFundNodeCreator(fundNodeCreator);
        nodeCreatorFactory.setHoldingsNodeCreator(holdingsNodeCreator);
        nodeCreatorFactory.setInvestorNodeCreator(investorNodeCreator);
    }

    @Test
    @DisplayName("should return investor node creator")
    void testForInvestorLevel() {
        Assertions.assertSame(investorNodeCreator, nodeCreatorFactory.getCreatorInstance(Level.INVESTOR));
    }

    @Test
    @DisplayName("should return fund node creator")
    void testForFundLevel() {
        Assertions.assertSame(fundNodeCreator, nodeCreatorFactory.getCreatorInstance(Level.FUND));
    }

    @Test
    @DisplayName("should return holdings node creator")
    void testForHoldingsLevel() {
        Assertions.assertSame(holdingsNodeCreator, nodeCreatorFactory.getCreatorInstance(Level.HOLDINGS));
    }

}
