package com.sapient.ws.impl;

import com.sapient.module.Level;
import com.sapient.module.exception.InvalidResourceException;
import com.sapient.service.MarketValueCalculator;
import com.sapient.ws.MarketValueCalculatorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/market-value")
public class MarketValueCalculatorControllerImpl implements MarketValueCalculatorController {

    private static Logger logger = LoggerFactory.getLogger(MarketValueCalculatorControllerImpl.class);

    private MarketValueCalculator marketValueCalculator;

    @Autowired
    public void setMarketValueCalculator(MarketValueCalculator marketValueCalculator) {
        this.marketValueCalculator = marketValueCalculator;
    }

    @Override
    @GetMapping(path = "/{label}/{name}")
    public ResponseEntity<Double> calculate(@PathVariable String label, @PathVariable String name,
                                            @RequestParam(name = "exclusive") String exclusiveHoldings) throws InvalidResourceException {
        logger.info("Executing::MarketValueCalculatorControllerImpl.calculate(label: {}, name: {})", label, name);

        Level level;
        if (label.equalsIgnoreCase(Level.INVESTOR.getName())) {
            level = Level.INVESTOR;
        } else if (label.equalsIgnoreCase(Level.FUND.getName())) {
            level = Level.FUND;
        } else {
            logger.error("invalid label to calculate market value");
            return ResponseEntity.notFound().build();
        }

        double marketValue = marketValueCalculator.calculate(level, name, getHoldingsSet(exclusiveHoldings));

        logger.info("Executing::MarketValueCalculatorControllerImpl.calculate(marketValue: {})", marketValue);
        return ResponseEntity.ok(marketValue);
    }

    private Set<String> getHoldingsSet(String exclusiveHoldings) {
        if (exclusiveHoldings != null && !exclusiveHoldings.isBlank()) {
            return new HashSet<>(Arrays.asList(exclusiveHoldings.split(",")));
        }
        return Collections.emptySet();
    }
}
