package com.sapient.service.impl;

import com.sapient.graphdb.FundNodeCreator;
import com.sapient.graphdb.HoldingsNodeCreator;
import com.sapient.graphdb.InvestorNodeCreator;
import com.sapient.graphdb.NodeCreator;
import com.sapient.module.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeCreatorFactory {

    private InvestorNodeCreator investorNodeCreator;
    private FundNodeCreator fundNodeCreator;
    private HoldingsNodeCreator holdingsNodeCreator;

    @Autowired
    public void setInvestorNodeCreator(InvestorNodeCreator investorNodeCreator) {
        this.investorNodeCreator = investorNodeCreator;
    }

    @Autowired
    public void setFundNodeCreator(FundNodeCreator fundNodeCreator) {
        this.fundNodeCreator = fundNodeCreator;
    }

    @Autowired
    public void setHoldingsNodeCreator(HoldingsNodeCreator holdingsNodeCreator) {
        this.holdingsNodeCreator = holdingsNodeCreator;
    }

    public NodeCreator getCreatorInstance(Level level) {
        switch (level) {
            case INVESTOR:
                return investorNodeCreator;
            case FUND:
                return fundNodeCreator;
            default:
                return holdingsNodeCreator;
        }
    }
}
