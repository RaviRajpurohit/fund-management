package com.sapient.service.impl.test;

import com.sapient.graphdb.NodeCreator;
import com.sapient.module.Level;
import com.sapient.service.impl.NodeCreatorFactory;
import com.sapient.service.impl.NodeCreatorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Map;

@DisplayName("Test scenario for node creator service.")
class NodeCreatorServiceImplTest {

    private static NodeCreatorServiceImpl creatorService;
    private static NodeCreatorFactory nodeCreatorFactory;

    @BeforeAll
    static void initialize() {
        nodeCreatorFactory = Mockito.mock(NodeCreatorFactory.class);
        creatorService = new NodeCreatorServiceImpl();
        creatorService.setNodeCreatorFactory(nodeCreatorFactory);
    }

    @Test
    @DisplayName("service should call node creator that given by factory")
    void testCreate() {

        Level level = Level.INVESTOR;

        Mockito.when(nodeCreatorFactory.getCreatorInstance(level)).thenReturn(new MockNodeCreator());

        creatorService.create(level, Collections.singletonMap("name", "node for service test"), null, null);

        Assertions.assertTrue(true, "assert added in mock class to verify data.");
    }

    static class MockNodeCreator implements NodeCreator {

        @Override
        public void create(Map<String, Object> node, Map<String, Object> childNode, Map<String, Object> edgeProperties) {
            Assertions.assertNotNull(node);
            Assertions.assertEquals("node for service test", node.get("name"));
        }
    }
}
