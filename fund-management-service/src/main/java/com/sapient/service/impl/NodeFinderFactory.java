package com.sapient.service.impl;

import com.sapient.graphdb.Finder;
import com.sapient.graphdb.FundFinder;
import com.sapient.graphdb.HoldingsFinder;
import com.sapient.graphdb.InvestorFinder;
import com.sapient.module.Level;
import com.sapient.module.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeFinderFactory {

    private InvestorFinder investorFinder;
    private FundFinder fundFinder;
    private HoldingsFinder holdingsFinder;

    @Autowired
    public void setFundFinder(FundFinder fundFinder) {
        this.fundFinder = fundFinder;
    }

    @Autowired
    public void setHoldingsFinder(HoldingsFinder holdingsFinder) {
        this.holdingsFinder = holdingsFinder;
    }

    @Autowired
    public void setInvestorFinder(InvestorFinder investorFinder) {
        this.investorFinder = investorFinder;
    }

    public Finder<? extends Node> getFinder(Level level) {
        switch (level) {
            case INVESTOR:
                return investorFinder;
            case FUND:
                return fundFinder;
            default:
                return holdingsFinder;
        }
    }
}
