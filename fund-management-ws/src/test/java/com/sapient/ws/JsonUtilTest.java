package com.sapient.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@DisplayName("Test scenario for Json util class")
class JsonUtilTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should return map for given json attributes")
    void testGetMapForGivenJson() {
        ObjectNode objectNode = objectMapper.createObjectNode();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", "Radha Rani");
        node.put("value", 20.0);
        objectNode.set("node", node);

        Map<String, Object> map = JsonUtil.getMap(objectNode, "node");
        Assertions.assertNotNull(map);
        Assertions.assertEquals(2, map.size());
        Assertions.assertEquals("Radha Rani", map.get("name"));
        Assertions.assertEquals(20.0, map.get("value"));
    }

    @Test
    @DisplayName("Should throw exception for unknown field")
    void testGetMapForUnknownField() {
        ObjectNode objectNode = objectMapper.createObjectNode();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("name", "Radha Rani");
        node.put("value", 20L);
        objectNode.set("node", node);

        Assertions.assertThrows(IllegalArgumentException.class, () -> JsonUtil.getMap(objectNode, "node"));
    }

    @Test
    @DisplayName("Should return empty map for given empty json")
    void testGetMapForEmptyJson() {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.set("node", null);

        Map<String, Object> map = JsonUtil.getMap(objectNode, "childNode");
        Assertions.assertNotNull(map);
        Assertions.assertEquals(0, map.size());

        Map<String, Object> map1 = JsonUtil.getMap(objectNode, "node");
        Assertions.assertNotNull(map1);
        Assertions.assertEquals(0, map1.size());
    }

}
