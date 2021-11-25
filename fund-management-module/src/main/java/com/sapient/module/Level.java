package com.sapient.module;

public enum Level {
    INVESTOR(1, "Investor"), FUND(2, "Fund"), HOLDINGS(3, "Holdings");

    private final int value;
    private final String name;

    Level(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static Level getLevel(int value) {
        switch (value) {
            case 1:
                return INVESTOR;
            case 2:
                return FUND;
            case 3:
                return HOLDINGS;
            default:
                throw new IllegalArgumentException("Invalid Level, currently we support only 1,2,3 level. Level: " + value);
        }
    }

    public String getName() {
        return name;
    }
}
