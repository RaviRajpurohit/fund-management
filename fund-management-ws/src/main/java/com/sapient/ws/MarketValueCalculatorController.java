package com.sapient.ws;

import com.sapient.module.exception.InvalidResourceException;
import org.springframework.http.ResponseEntity;

/**
 * Rest end-point to calculate the market value for any fund or investor.
 */
public interface MarketValueCalculatorController {

    /**
     * API to calculate the market value for given level
     *
     * @param label             name of node, supported: investor, fund
     * @param name              attribute value to calculate market value.
     * @param exclusiveHoldings comma separated holdings name (optional), if provided these values will be removed from calculation.
     * @return calculated market value as per given parameter(s)
     */
    ResponseEntity<Double> calculate(String label, String name, String exclusiveHoldings) throws InvalidResourceException;

}
