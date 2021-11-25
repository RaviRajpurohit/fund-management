package com.sapient.service.impl.test;

import com.sapient.graphdb.FundFinder;
import com.sapient.graphdb.HoldingsFinder;
import com.sapient.graphdb.InvestorFinder;
import com.sapient.module.Level;
import com.sapient.service.impl.NodeFinderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("Test scenario for finder factory")
class NodeFinderFactoryTest {

    private static NodeFinderFactory nodeFinderFactory;
    private static InvestorFinder investorFinder;
    private static FundFinder fundFinder;
    private static HoldingsFinder holdingsFinder;

    @BeforeAll
    static void initialize() {
        investorFinder = Mockito.mock(InvestorFinder.class);
        fundFinder = Mockito.mock(FundFinder.class);
        holdingsFinder = Mockito.mock(HoldingsFinder.class);

        nodeFinderFactory = new NodeFinderFactory();
        nodeFinderFactory.setFundFinder(fundFinder);
        nodeFinderFactory.setHoldingsFinder(holdingsFinder);
        nodeFinderFactory.setInvestorFinder(investorFinder);
    }

    @Test
    @DisplayName("should return investor node creator")
    void testForInvestorLevel() {
        Assertions.assertSame(investorFinder, nodeFinderFactory.getFinder(Level.INVESTOR));
    }

    @Test
    @DisplayName("should return fund node creator")
    void testForFundLevel() {
        Assertions.assertSame(fundFinder, nodeFinderFactory.getFinder(Level.FUND));
    }

    @Test
    @DisplayName("should return holdings node creator")
    void testForHoldingsLevel() {
        Assertions.assertSame(holdingsFinder, nodeFinderFactory.getFinder(Level.HOLDINGS));
    }

}
