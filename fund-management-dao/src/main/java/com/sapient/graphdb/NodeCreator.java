package com.sapient.graphdb;

import java.util.Map;

/**
 * @author Ravi
 * DAO layer to interact with graph db based on parameter.
 */
public interface NodeCreator {

    /**
     * Method to create varies nodes and relation between them.
     *
     * @param node           level node. ex: for Level: 1 node will be investor, for level: 2 node will be fund etc.
     * @param childNode      level child node. ex: for Level: 1 childNode will be fund, for level: 2 childNode will be holdings etc.
     * @param edgeProperties additional properties when edge need attribute to create relation.
     */
    void create(Map<String, Object> node, Map<String, Object> childNode,
                Map<String, Object> edgeProperties);
}
