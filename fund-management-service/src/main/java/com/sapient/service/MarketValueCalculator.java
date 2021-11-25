package com.sapient.service;

import com.sapient.module.Level;
import com.sapient.module.exception.InvalidResourceException;

import java.util.Set;

/**
 * @author Ravi
 * Service layer to calculate market values for different node.
 */
public interface MarketValueCalculator {

    /**
     * Method to calculate market value as per user criteria
     *
     * @param level               {@link Level}
     * @param name                node's attribute name's value
     * @param holdingsToExclusion collection of holdings name to exclude from output
     * @return calculate market value
     */
    double calculate(Level level, String name, Set<String> holdingsToExclusion) throws InvalidResourceException;

}
