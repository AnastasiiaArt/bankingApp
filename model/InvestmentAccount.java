package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public class InvestmentAccount {

    private BigDecimal notInvested;

    private final EnumMap<Fund, BigDecimal> holdings;

    private final EnumSet<Fund> activatedFunds;

    public InvestmentAccount() {

        this.notInvested = money("0.00");
        this.holdings = new EnumMap<>(Fund.class);
        this.activatedFunds = EnumSet.noneOf(Fund.class);

    }

    public BigDecimal getNotInvested() {

        return notInvested;

    }

    public void addToNotInvested(BigDecimal amount) {

        notInvested = money(notInvested.add(amount));

    }

    public void subtractFromNotInvested(BigDecimal amount) {

        notInvested = money(notInvested.subtract(amount));

    }

    private BigDecimal money(String value) {

        return money(new BigDecimal(value));

    }

    private BigDecimal money(BigDecimal value) {

        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public void invest(Fund fund, BigDecimal amount) {

        subtractFromNotInvested(amount);

        BigDecimal current = getHolding(fund);
        holdings.put(fund, money(current.add(amount)));
        activatedFunds.add(fund);

    }

    public void applyGrowth() {

        for (Fund fund : activatedFunds) {
            BigDecimal current = getHolding(fund);
            BigDecimal grown = current.multiply(fund.getGrowthFactor());
            holdings.put(fund, money(grown));
        }

    }

    public void withdrawAllInvestmentsToNotInvested() {

        BigDecimal total = money("0.00");
        for (Fund fund : activatedFunds) {
            total = money(total.add(getHolding(fund)));
            holdings.put(fund, money("0.00"));
        }

        addToNotInvested(total);

    }

    public BigDecimal getHolding(Fund fund) {

        BigDecimal v = holdings.get(fund);
        if (v == null) {
            return money("0.00");
        }

        return v;
    }

    public Set<Fund> getActivatedFunds() {
        return EnumSet.copyOf(activatedFunds);
    }

}