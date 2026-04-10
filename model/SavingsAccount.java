package model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SavingsAccount {

    private BigDecimal balance;

    public SavingsAccount(BigDecimal initialBalance) {
        this.balance = money(initialBalance);
    }

    public BigDecimal getBalance() {
        return balance;

    }

    public void deposit(BigDecimal amount) {
        balance = money(balance.add(amount));

    }

    public void withdraw(BigDecimal amount) {
        balance = money(balance.subtract(amount));
    }

    public void applyInterest(BigDecimal rate) {

        BigDecimal factor = BigDecimal.ONE.add(rate);
        balance = money(balance.multiply(factor));

    }

    private BigDecimal money(BigDecimal value) {

        return value.setScale(2, RoundingMode.HALF_UP);

    }

}