package model;

import java.math.BigDecimal;

public enum Fund {

    LOW_RISK("1.02"),
    MEDIUM_RISK("1.05"),
    HIGH_RISK("1.10");

    private final BigDecimal growthFactor;

    Fund(String growthFactor) {

        this.growthFactor = new BigDecimal(growthFactor);
    }

    public BigDecimal getGrowthFactor() {
        return growthFactor;
    }

}