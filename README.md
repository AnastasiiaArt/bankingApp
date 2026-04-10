 Banking App 💰

A simple command-line banking application where users can manage their money, transfer funds, and invest in different risk-level funds — all with guaranteed growth.

📌 Overview

Banking App is a Java-based CLI (Command Line Interface) application that simulates a banking system. Users can log in, manage savings and investments, and perform transactions between accounts and other users.

The system is designed to demonstrate:

Object-oriented programming (OOP)
Clean project structure
Exception handling
Working with BigDecimal for financial accuracy
👥 Supported Users

The application supports the following predefined users:

Alice
Bob
Charlie
Diana

Each user starts with:

💵 $1000 in cash
🏦 Empty savings account
📈 Empty investment account
🧭 Features
🔐 Login System
Users log in using their name
Only valid users are allowed
Invalid usernames are handled gracefully
📋 Menu System

After logging in, users see:

 --- Banking App Menu ---
1. Show balance
2. Deposit money
3. Withdraw money
4. Send money to a person
5. Invest in funds
6. Transfer between accounts
7. Withdraw all investments
8. Logout
9. Exit
🏦 Account Management

Each user has:

Cash (initially $1000)
Savings Account
Investment Account
Key Operations:
Deposit cash → savings account
Withdraw savings → cash
Transfer between savings and investment accounts
Send money to other users
💸 Interest System
Savings account earns 1% interest
Interest is applied automatically every time the balance is viewed
📈 Investment Funds

Users can invest from their investment account into:

Fund Type	Growth Rate
LOW_RISK	2%
MEDIUM_RISK	5%
HIGH_RISK	10%
Notes:
Gains are applied when viewing balance
Users can withdraw all investments anytime back to their investment account
🔄 Session Management
Users stay logged in until they:
Logout
Exit the application
Switching users is supported
⚠️ Error Handling

The application handles:

Invalid usernames
Insufficient funds
Invalid input formats
EOF (Ctrl+D) safely using Scanner.hasNextLine()

Custom exception:

InvalidAmountException
🗂️ Project Structure
bankingApp
├── BankingApp.java
├── model
│   ├── Account.java
│   ├── Fund.java
│   ├── InvestmentAccount.java
│   ├── SavingsAccount.java
│   └── User.java
├── exception
│   └── InvalidAmountException.java
└── service
    └── BankingService.java
⚙️ Technical Notes
All monetary values use BigDecimal
Only one Scanner instance is used
❗ No static methods allowed except main
❗ Do NOT use System.exit()
🚀 How to Run

Compile the project:

javac BankingApp.java

Run the application:

java BankingApp
Follow on-screen instructions
🧪 Testing Notes
Output must match expected format exactly
Pay attention to:
Spaces
New lines
Menu formatting