package com.sapient.graphdb;

import com.sapient.module.Node;

import java.util.Set;

/**
 * @author Ravi
 * DAO layer to extract graph info.
 */
public interface Finder<T extends Node> {

    /**
     * method to extract data on the basis of given parameter
     *
     * @param name                node's attribute name
     * @param holdingsToExclusion collection of holdings name to exclude from output
     * @return node with its relationship(s)
     */
    T find(String name, Set<String> holdingsToExclusion);

}
