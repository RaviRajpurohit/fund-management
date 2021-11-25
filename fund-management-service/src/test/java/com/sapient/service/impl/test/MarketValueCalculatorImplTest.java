package com.sapient.service.impl.test;

import com.sapient.graphdb.FundFinder;
import com.sapient.graphdb.InvestorFinder;
import com.sapient.module.Fund;
import com.sapient.module.Holdings;
import com.sapient.module.Investor;
import com.sapient.module.Level;
import com.sapient.module.exception.InvalidResourceException;
import com.sapient.service.impl.MarketValueCalculatorImpl;
import com.sapient.service.impl.NodeFinderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@DisplayName("Test scenario for market value calculator service")
class MarketValueCalculatorImplTest {


    private static MarketValueCalculatorImpl marketValueCalculator;
    private static InvestorFinder investorFinder;
    private static FundFinder fundFinder;

    @BeforeAll
    static void initialize() {
        marketValueCalculator = new MarketValueCalculatorImpl();
        investorFinder = Mockito.mock(InvestorFinder.class);
        fundFinder = Mockito.mock(FundFinder.class);

        NodeFinderFactory finderFactory = new NodeFinderFactory();
        finderFactory.setFundFinder(fundFinder);
        finderFactory.setInvestorFinder(investorFinder);

        marketValueCalculator.setFinderFactory(finderFactory);
    }

    @DisplayName("should calculate market value for given investor")
    @Test
    void calculateForInvestor() throws InvalidResourceException {
        Mockito.when(investorFinder.find("Hari", null)).thenReturn(investor());
        double marketValue = marketValueCalculator.calculate(Level.INVESTOR, "Hari", null);
        Assertions.assertEquals(10139.00, marketValue);
    }

    @DisplayName("should calculate market value for given fund")
    @Test
    void calculateForFund() throws InvalidResourceException {
        Mockito.when(fundFinder.find("Sapient", null)).thenReturn(fund());
        double marketValue = marketValueCalculator.calculate(Level.FUND, "Sapient", null);
        Assertions.assertEquals(2030.00, marketValue);
    }

    @DisplayName("should throw exception when node not present")
    @Test
    void calculateForInvalidNode() {
        Mockito.when(investorFinder.find("Shiv", null)).thenReturn(null);
        Assertions.assertThrows(InvalidResourceException.class, () -> marketValueCalculator.calculate(Level.INVESTOR, "Shiv", null));
    }

    private Investor investor() {
        Investor investor = new Investor();
        investor.setName("Hari");

        Set<Fund> funds = new HashSet<>(2);
        Fund fund = new Fund();
        fund.setName("Group");

        Holdings holdings = new Holdings();
        holdings.setName("Group-Licence");
        holdings.setValue(90.10);

        fund.setHoldingsWithQuantity(Collections.singletonMap(holdings, 90));
        funds.add(fund);
        funds.add(fund());

        investor.setFunds(funds);

        return investor;
    }

    private Fund fund() {
        Fund fund = new Fund();
        fund.setName("Sapient");

        Holdings holdings = new Holdings();
        holdings.setName("Licence");
        holdings.setValue(20.3);

        fund.setHoldingsWithQuantity(Collections.singletonMap(holdings, 100));
        return fund;
    }


}
