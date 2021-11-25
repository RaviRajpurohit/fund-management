package com.sapient.ws.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.sapient.module.Level;
import com.sapient.service.NodeCreatorService;
import com.sapient.ws.JsonUtil;
import com.sapient.ws.NodeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/node")
public class NodeControllerImpl implements NodeController {

    private static final Logger logger = LoggerFactory.getLogger(NodeControllerImpl.class);

    private NodeCreatorService nodeCreatorService;

    @Autowired
    public void setNodeCreatorService(NodeCreatorService nodeCreatorService) {
        this.nodeCreatorService = nodeCreatorService;
    }

    @Override
    @PostMapping(path = "/{level}")
    public ResponseEntity<Object> create(@PathVariable int level, @RequestBody JsonNode nodeDetails,
                                 @RequestParam Map<String, Object> additionalInfo) {
        logger.info("Executing::NodeControllerImpl.create(level: {})", level);
        if (level < 1 || level > 3) {
            throw new IllegalArgumentException("Invalid level, supported level are 1: Investor, 2: Fund, 3: Holdings. given level: " + level);
        }

        Level nodeLevel = Level.getLevel(level);

        nodeCreatorService.create(nodeLevel, JsonUtil.getMap(nodeDetails, "node"),
                JsonUtil.getMap(nodeDetails, "childNode"), additionalInfo);

        logger.info("Exiting::NodeControllerImpl.create()");
        return ResponseEntity.ok("Entry booked successfully.");
    }
}
