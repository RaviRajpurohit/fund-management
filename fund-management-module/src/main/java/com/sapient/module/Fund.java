package com.sapient.module;

import java.util.Map;

public class Fund extends Node {

    private Map<Holdings, Integer> holdingsWithQuantity;

    public Map<Holdings, Integer> getHoldingsWithQuantity() {
        return holdingsWithQuantity;
    }

    public void setHoldingsWithQuantity(Map<Holdings, Integer> holdingsWithQuantity) {
        this.holdingsWithQuantity = holdingsWithQuantity;
    }
}
