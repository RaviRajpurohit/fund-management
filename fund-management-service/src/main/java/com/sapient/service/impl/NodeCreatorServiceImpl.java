package com.sapient.service.impl;

import com.sapient.graphdb.NodeCreator;
import com.sapient.module.Level;
import com.sapient.service.NodeCreatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NodeCreatorServiceImpl implements NodeCreatorService {

    private static final Logger logger = LoggerFactory.getLogger(NodeCreatorServiceImpl.class);

    private NodeCreatorFactory nodeCreatorFactory;

    @Autowired
    public void setNodeCreatorFactory(NodeCreatorFactory nodeCreatorFactory) {
        this.nodeCreatorFactory = nodeCreatorFactory;
    }


    @Override
    public void create(Level level, Map<String, Object> node, Map<String, Object> childNode, Map<String, Object> additionalInfo) {
        logger.info("Executing::NodeCreatorServiceImpl.create()");

        NodeCreator nodeCreator = nodeCreatorFactory.getCreatorInstance(level);

        nodeCreator.create(node, childNode, additionalInfo);

        logger.info("Exiting::NodeCreatorServiceImpl.create()");
    }
}
