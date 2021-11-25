package com.sapient.service;

import com.sapient.module.Level;

import java.util.Map;

/**
 * @author Ravi
 * Service interface to handle node related operation.
 */
public interface NodeCreatorService {

    /**
     * method to create or modify the node and their relationship
     *
     * @param level          {@link Level}
     * @param node           node's attributes
     * @param childNode      childNode's attributes
     * @param additionalInfo relationship edge's attributes
     */
    void create(Level level, Map<String, Object> node, Map<String, Object> childNode, Map<String, Object> additionalInfo);
}
