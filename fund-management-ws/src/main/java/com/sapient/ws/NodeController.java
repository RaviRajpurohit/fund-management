package com.sapient.ws;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * @author Ravi
 * Rest end-point to handle user operation related to node create or modify with their relationship.
 */
public interface NodeController {

    /**
     * method to create either node or relationship.
     *
     * @param level          this identifies the node's nature and it's relationship
     * @param nodeDetails    json will contain the Node and Child Node info as per the level
     * @param additionalInfo additionalInfo to add attributes in edge
     */
    ResponseEntity<Object> create(int level, JsonNode nodeDetails, Map<String, Object> additionalInfo);

}
