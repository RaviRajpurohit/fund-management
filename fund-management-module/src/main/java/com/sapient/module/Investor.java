package com.sapient.module;

import java.util.Set;

public class Investor extends Node {

    private Set<Fund> funds;

    public Set<Fund> getFunds() {
        return funds;
    }

    public void setFunds(Set<Fund> funds) {
        this.funds = funds;
    }
}
