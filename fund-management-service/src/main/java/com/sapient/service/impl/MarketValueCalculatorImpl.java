package com.sapient.service.impl;

import com.sapient.module.*;
import com.sapient.module.exception.InvalidResourceException;
import com.sapient.service.MarketValueCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class MarketValueCalculatorImpl implements MarketValueCalculator {
    private static final Logger logger = LoggerFactory.getLogger(MarketValueCalculatorImpl.class);

    private NodeFinderFactory finderFactory;

    @Autowired
    public void setFinderFactory(NodeFinderFactory finderFactory) {
        this.finderFactory = finderFactory;
    }

    @Override
    public double calculate(Level level, String name, Set<String> holdingsToExclusion) throws InvalidResourceException {
        logger.info("Executing::MarketValueCalculatorImpl.calculate(level: {}, name: {})", level, name);

        Node node = finderFactory.getFinder(level).find(name, holdingsToExclusion);

        if (node == null) {
            throw new InvalidResourceException("For given label and name node not found.");
        }

        double marketValue;
        switch (level) {
            case INVESTOR:
                marketValue = calculateForInvestor((Investor) node);
                break;
            case FUND:
                marketValue = calculateForFund((Fund) node);
                break;
            default:
                throw new IllegalArgumentException("not supported.");
        }

        logger.info("Executing::MarketValueCalculatorImpl.calculate(marketValue: {})", marketValue);
        return marketValue;
    }

    private double calculateForInvestor(Investor investor) {
        double total = 0;
        for (Fund fund : investor.getFunds()) {
            total += calculateForFund(fund);
        }
        return total;
    }

    private double calculateForFund(Fund fund) {
        double total = 0;
        for (Map.Entry<Holdings, Integer> entry : fund.getHoldingsWithQuantity().entrySet()) {
            total += (entry.getValue() * entry.getKey().getValue());
        }
        return total;
    }
}
