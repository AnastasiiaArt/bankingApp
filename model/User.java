package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class User {

    private final String name;
    private BigDecimal cashOnHand;
    private final SavingsAccount savings;
    private final InvestmentAccount investment;

    public User(String name) {

        this.name = name;
        this.cashOnHand = money("1000.00");
        this.savings = new SavingsAccount(money("0.00"));
        this.investment = new InvestmentAccount();

    }

    public String getName() {
        return name;

    }

    public BigDecimal getCashOnHand() {
        return cashOnHand;

    }

    public SavingsAccount getSavings() {
        return savings;

    }

    public InvestmentAccount getInvestment() {
        return investment;

    }

    public void addCash(BigDecimal amount) {
        cashOnHand = money(cashOnHand.add(amount));
    }

    public void subtractCash(BigDecimal amount) {
        cashOnHand = money(cashOnHand.subtract(amount));

    }

    private BigDecimal money(String value) {
        return money(new BigDecimal(value));

    }

    private BigDecimal money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

}
