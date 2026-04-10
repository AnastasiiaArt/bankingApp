package service;

import model.User;
import model.Fund;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class BankingService {

    private final Scanner scanner;
    private final Map<String, User> usersByName;
    private final String LOGIN_PROMPT = "Enter your name to login: ";
    private final String USER_NOT_FOUND = "User not found. Please try again.";
    private final String ENTER_CHOICE = "Enter your choice: ";
    private final String INVALID_CHOICE = "Invalid choice. Please try again.";
    private final String DEPOSIT_PROMPT = "Enter amount to deposit to savings account: $";
    private final String SAVINGS_BALANCE_LABEL = "Savings account balance: ";
    private final String INVESTMENT_BALANCE_LABEL = "Investment account balance:";
    private final String NOT_INVESTED_LABEL = "* Not Invested: ";
    private final String DEPOSIT_SUCCESS = "Deposit successful.";
    private final String DEPOSIT_INVALID_AMOUNT = "Deposit failed: invalid amount.";
    private final String DEPOSIT_NEGATIVE = "Deposit failed: amount must be positive";
    private final String DEPOSIT_INSUFFICIENT_CASH = "Deposit failed: Insufficient cash on hand";
    private final String WITHDRAW_PROMPT = "Enter amount to withdraw from savings account: $";
    private final String WITHDRAW_INVALID_AMOUNT = "Withdrawal failed: invalid amount.";
    private final String WITHDRAW_SUCCESS = "Withdrawal successful.";
    private final String WITHDRAW_FAIL_NEGATIVE = "Withdrawal failed: amount must be positive";
    private final String WITHDRAW_INSUFFICIENT_FUNDS = "Withdrawal failed: Insufficient funds";
    private final String WITHDRAW_ALL_SUCCESS = "All investments have been withdrawn and added to your investment account balance.";
    private final String TRANSFER_AMOUNT_PROMPT = "Enter amount to transfer: ";
    private final String TRANSFER_INVALID_CHOICE = "$Invalid choice.";
    private final String TRANSFER_INVALID_AMOUNT = "$Invalid choice.";
    private final String TRANSFER_NEGATIVE = "$Transfer failed: amount must be positive";
    private final String TRANSFER_INSUFFICIENT_SAVINGS = "$Transfer failed: Insufficient funds";
    private final String TRANSFER_INSUFFICIENT_NOTINV = "$Transfer failed: Insufficient funds";
    private final String TRANSFER_SUCCESS_TO_INV = "Transfer successful: savings to investment.";
    private final String TRANSFER_SUCCESSFUL = "Transfer successful: investment to savings.";
    private final String INVEST_FUNDS_HEADER = "Available funds:";
    private final String INVEST_FUND_PROMPT = "Enter fund to invest in: ";
    private final String INVEST_AMOUNT_PROMPT = "Enter amount to invest: ";
    private final String INVEST_INVALID_FUND = "Invalid fund.";
    private final String INVEST_INVALID_AMOUNT = "Investment failed: invalid amount.";
    private final String INVEST_NEGATIVE = "$Failed to invest: amount must be positive";
    private final String INVEST_INSUFFICIENT = "$Failed to invest: Insufficient funds";
    private final String INVEST_SUCCESS_PREFIX = "Investment successful: ";
    private final String SEND_RECIPIENTS_HEADER = "Available recipients:";
    private final String SEND_RECIPIENT_PROMPT = "Enter recipient's name: ";
    private final String SEND_AMOUNT_PROMPT = "Enter amount to send: $";
    private final String SEND_INVALID_RECIPIENT = "Invalid recipient.";
    private final String SEND_INVALID_AMOUNT = "Send failed: invalid amount.";
    private final String SEND_NEGATIVE = "Failed to send money: amount must be positive";
    private final String SEND_INSUFFICIENT = "Failed to send money: Insufficient funds";
    private final String SEND_SUCCESSFUL = "Send successful: ";
    private final String EXIT_MESSAGE = "Thank you for using our banking app. Goodbye!";
    private final String LOGOUT_MESSAGE = "You have been logged out.";

    private void printMenu() {
        System.out.println("--- Banking App Menu ---");
        System.out.println("1. Show balance");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Send money to a person");
        System.out.println("5. Invest in funds");
        System.out.println("6. Transfer between accounts");
        System.out.println("7. Withdraw all investments");
        System.out.println("8. Logout");
        System.out.println("9. Exit");

    }

    private void printTransferMenu() {
        System.out.println("1. Transfer from savings to investment");
        System.out.println("2. Transfer from investment to savings");

    }

    private String readLineWithPrompt(String prompt) {

        System.out.print(prompt);
        System.out.flush();
        if (!scanner.hasNextLine()) {

            return null;
        }

        return scanner.nextLine();

    }

    private User loginOrNullOnEOF() {

        while (true) {

            String nameLine = readLineWithPrompt(LOGIN_PROMPT);
            if (nameLine == null) {
                return null;
            }

            String name = nameLine.trim();
            User user = usersByName.get(name);

            if (user != null) {
                System.out.println("Welcome, " + name + "!");
                System.out.println();
                return user;
            }

            System.out.println(USER_NOT_FOUND);

        }

    }

    private boolean sessionLoop(User user) {

        while (true) {

            printMenu();
            String choiceLine = readLineWithPrompt(ENTER_CHOICE);
            if (choiceLine == null) {
                return true;
            }
            int choice;
            try {
                choice = Integer.parseInt(choiceLine.trim());
            } catch (NumberFormatException e) {
                System.out.println(INVALID_CHOICE);
                System.out.println();
                continue;
            }

            if (choice < 1 || choice > 9) {
                System.out.println(INVALID_CHOICE);
                System.out.println();
                continue;
            }
            if (choice == 9) {
                System.out.println(EXIT_MESSAGE);
                return true;
            }
            if (choice == 8) {
                System.out.println(LOGOUT_MESSAGE);
                return false;
            }
            if (choice == 1) {
                handleShowBalance(user);
                System.out.println();
                continue;
            }

            if (choice == 2) {

                String amtLine = readLineWithPrompt(DEPOSIT_PROMPT);
                if (amtLine == null) {
                    System.out.println();
                    return true;
                }

                BigDecimal amount;
                try {
                    amount = money(new BigDecimal(amtLine.trim()));
                } catch (NumberFormatException e) {
                    System.out.println();
                    continue;
                }

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println(DEPOSIT_NEGATIVE);
                    System.out.println();
                    continue;
                }

                if (amount.compareTo(user.getCashOnHand()) > 0) {
                    System.out.println(DEPOSIT_INSUFFICIENT_CASH);
                    System.out.println();
                    continue;
                }

                user.subtractCash(amount);
                user.getSavings().deposit(amount);

                System.out.println(DEPOSIT_SUCCESS);
                System.out.println();

                continue;
            }

            if (choice == 3) {
                String amtLine = readLineWithPrompt(WITHDRAW_PROMPT);
                if (amtLine == null) {
                    return true;
                }

                BigDecimal amount;
                try {
                    amount = money(new BigDecimal(amtLine.trim()));
                } catch (NumberFormatException e) {
                    System.out.println(WITHDRAW_INVALID_AMOUNT);
                    System.out.println();
                    continue;
                }

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println(WITHDRAW_FAIL_NEGATIVE);
                    System.out.println();
                    continue;
                }

                if (amount.compareTo(user.getSavings().getBalance()) > 0) {
                    System.out.println(WITHDRAW_INSUFFICIENT_FUNDS);
                    System.out.println();
                    continue;
                }

                user.addCash(amount);
                user.getSavings().withdraw(amount);

                System.out.println(WITHDRAW_SUCCESS);
                System.out.println();

                continue;

            }

            if (choice == 4) {
                boolean shouldExit = handleSendMoney(user);
                if (shouldExit) {
                    return true;
                }
                continue;
            }

            if (choice == 5) {
                boolean shouldExit = handleInvest(user);
                if (shouldExit) {
                    return true;
                }
                continue;
            }

            if (choice == 6) {
                boolean shouldExit = handleTransfer(user);
                if (shouldExit) {
                    return true;
                }
                continue;
            }

            if (choice == 7) {
                handleWithdrawAllInvestments(user);
                continue;
            }

        }

    }

    public BankingService(Scanner scanner) {

        this.scanner = scanner;
        this.usersByName = new HashMap<>();
        usersByName.put("Alice", new User("Alice"));
        usersByName.put("Bob", new User("Bob"));
        usersByName.put("Charlie", new User("Charlie"));
        usersByName.put("Diana", new User("Diana"));

    }

    public void run() {

        while (true) {
            User user = loginOrNullOnEOF();
            if (user == null) {
                return;
            }

            boolean shouldExit = sessionLoop(user);
            if (shouldExit) {
                return;
            }
        }

    }

    private BigDecimal money(BigDecimal v) {

        return v.setScale(2, RoundingMode.HALF_UP);
    }

    private String fmtBalance(BigDecimal v) {

        return "$" + v.toPlainString();
    }

    private void handleShowBalance(User user) {
        user.getSavings().applyInterest(new BigDecimal("0.01"));
        user.getInvestment().applyGrowth();

        System.out.println(SAVINGS_BALANCE_LABEL + fmtBalance(user.getSavings().getBalance()));
        System.out.println(INVESTMENT_BALANCE_LABEL);
        System.out.println(NOT_INVESTED_LABEL + fmtBalance(user.getInvestment().getNotInvested()));

        for (Fund fund : Fund.values()) {
            if (user.getInvestment().getActivatedFunds().contains(fund)) {
                System.out.println("* " + fund.name() + ": " + fmtBalance(user.getInvestment().getHolding(fund)));
            }
        }

    }

    private boolean handleTransfer(User user) {
        printTransferMenu();

        String choiceLine = readLineWithPrompt(ENTER_CHOICE);
        if (choiceLine == null) {
            return true;
        }

        int transferChoice;
        try {
            transferChoice = Integer.parseInt(choiceLine.trim());
        } catch (NumberFormatException e) {
            System.out.println(TRANSFER_INVALID_CHOICE);
            System.out.println();
            return false;
        }

        if (transferChoice != 1 && transferChoice != 2) {
            String ignored = readLineWithPrompt(TRANSFER_AMOUNT_PROMPT);
            if (ignored == null) {
                return false;
            }
            System.out.println("$Invalid choice.");
            System.out.println();
            return false;
        }

        String amtLine = readLineWithPrompt(TRANSFER_AMOUNT_PROMPT);
        if (amtLine == null) {
            return true;
        }

        BigDecimal amount;
        try {
            amount = money(new BigDecimal(amtLine.trim()));

        } catch (NumberFormatException e) {
            System.out.println(TRANSFER_INVALID_AMOUNT);
            System.out.println();
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println(TRANSFER_NEGATIVE);
            System.out.println();
            return false;
        }

        if (transferChoice == 1) {
            if (amount.compareTo(user.getSavings().getBalance()) > 0) {
                System.out.println(TRANSFER_INSUFFICIENT_SAVINGS);
                System.out.println();
                return false;

            }

            user.getSavings().withdraw(amount);
            user.getInvestment().addToNotInvested(amount);

            System.out.println("$Successfully transferred " + fmtTxn(amount) + " to investment account.");
            System.out.println();
            return false;

        }

        if (amount.compareTo(user.getInvestment().getNotInvested()) > 0) {
            System.out.println(TRANSFER_INSUFFICIENT_NOTINV);
            System.out.println();
            return false;
        }

        user.getInvestment().subtractFromNotInvested(amount);
        user.getSavings().deposit(amount);

        System.out.println("$Successfully transferred " + fmtTxn(amount) + " to savings account.");
        System.out.println();
        return false;

    }

    private boolean handleInvest(User user) {

        System.out.println(INVEST_FUNDS_HEADER);
        for (Fund f : Fund.values()) {
            System.out.println(f.name());
        }

        String fundLine = readLineWithPrompt(INVEST_FUND_PROMPT);
        if (fundLine == null) {
            System.out.println();
            return false;
        }

        Fund fund;
        try {
            fund = Fund.valueOf(fundLine.trim().toUpperCase());

        } catch (IllegalArgumentException e) {
            System.out.println(INVEST_INVALID_FUND);
            System.out.println();
            return false;
        }

        String amtLine = readLineWithPrompt(INVEST_AMOUNT_PROMPT);
        if (amtLine == null) {
            return false;
        }

        BigDecimal amount;
        try {
            amount = money(new BigDecimal(amtLine.trim()));
        } catch (NumberFormatException e) {
            System.out.println(INVEST_INVALID_AMOUNT);
            System.out.println();
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println(INVEST_NEGATIVE);
            System.out.println();
            return false;
        }

        if (amount.compareTo(user.getInvestment().getNotInvested()) > 0) {
            System.out.println(INVEST_INSUFFICIENT);
            System.out.println();
            return false;
        }

        user.getInvestment().invest(fund, amount);

        System.out.println("$Successfully invested " + fmtTxn(amount) + " in " + fund.name() + " fund");
        System.out.println();
        return false;

    }

    private void handleWithdrawAllInvestments(User user) {
        user.getInvestment().withdrawAllInvestmentsToNotInvested();
        System.out.println(WITHDRAW_ALL_SUCCESS);
        System.out.println();
    }

    private boolean handleSendMoney(User sender) {
        System.out.println(SEND_RECIPIENTS_HEADER);

        String[] ordered = { "Alice", "Bob", "Charlie", "Diana" };

        for (String name : ordered) {
            if (!name.equals(sender.getName())) {
                System.out.println(name);
            }
        }

        String recipientLine = readLineWithPrompt(SEND_RECIPIENT_PROMPT);
        if (recipientLine == null) {
            return true;
        }

        String recipientName = recipientLine.trim();
        User recipient = usersByName.get(recipientName);

        if (recipient == null || recipientName.equals(sender.getName())) {
            System.out.println(SEND_INVALID_RECIPIENT);
            System.out.println();
            return false;
        }

        String amtLine = readLineWithPrompt(SEND_AMOUNT_PROMPT);
        if (amtLine == null) {
            return true;
        }

        BigDecimal amount;
        try {
            amount = money(new BigDecimal(amtLine.trim()));
        } catch (NumberFormatException e) {
            System.out.println(SEND_INVALID_AMOUNT);
            System.out.println();
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println(SEND_NEGATIVE);
            System.out.println();
            return false;
        }

        if (amount.compareTo(sender.getSavings().getBalance()) > 0) {
            System.out.println(SEND_INSUFFICIENT);
            System.out.println();
            return false;
        }

        sender.getSavings().withdraw(amount);
        recipient.getSavings().deposit(amount);

        System.out.println("Sent " + fmtTxn(amount) + " to " + recipient.getName());
        System.out.println();
        return false;

    }

    private String fmtTxn(BigDecimal v) {
        return "$" + v.stripTrailingZeros().toPlainString();
    }

}
