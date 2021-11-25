package com.sapient.ws;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtil {
    private JsonUtil() {
    }

    public static Map<String, Object> getMap(JsonNode nodeDetails, String fieldName) {
        JsonNode node = nodeDetails.get(fieldName);

        if (node == null || node.isNull()) {
            //when field not present
            return Collections.emptyMap();
        }

        Map<String, Object> map = new HashMap<>(node.size());
        Iterator<String> fieldNames = node.fieldNames();

        while (fieldNames.hasNext()) {
            String currentFieldName = fieldNames.next();
            map.put(currentFieldName, value(node.get(currentFieldName)));
        }

        return map;
    }

    private static Object value(JsonNode node) {
        if (node.isDouble()) {
            return node.asDouble();
        } else if (node.isTextual()) {
            return node.asText();
        } else {
            throw new IllegalArgumentException("Invalid field value.");
        }
    }
}
