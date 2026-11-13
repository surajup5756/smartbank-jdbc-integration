# SmartBank — Console Banking System

## About this project

SmartBank is a small banking-system simulation written in Java. I built it as a console application so the main focus stays on Java fundamentals: classes, inheritance, collections, validation and exception handling.

It is a learning project, not a real banking application. It does not connect to a bank or move real money.

## What the program can do

- Open Savings and Current accounts.
- Assign an account number automatically.
- Deposit money and withdraw money.
- Transfer money between two different accounts.
- Show an account's current balance and transaction history.
- Print a statement in the console.
- Block, unblock or close an account.
- Show a short summary of the accounts managed by the bank.
- Export a statement to a CSV file.

The account list is kept in a `HashMap`. Each account keeps its own transaction list in an `ArrayList`.

## Account rules

| Account type | Rule used in this project |
|---|---|
| Savings | A withdrawal or outgoing transfer must leave at least the configured minimum balance. |
| Current | The balance may go below zero, but it cannot go below the configured overdraft limit. |

A deposit, withdrawal or transfer amount must be greater than zero and may contain no more than two decimal places. A transfer is rejected if either account is missing, blocked, closed, or if the source account would break its balance rule.

The program uses `BigDecimal` for money rather than `double`. This avoids the rounding surprises that can happen with floating-point numbers.

## Project layout

```text
SmartBank/
├── src/
│   └── com/smartbank/
│       ├── main/
│       │   └── Main.java
│       ├── models/
│       │   ├── AccountStatus.java
│       │   ├── BankAccount.java
│       │   ├── SavingsAccount.java
│       │   ├── CurrentAccount.java
│       │   └── Transaction.java
│       ├── services/
│       │   ├── Bank.java
│       │   └── StatementService.java
│       └── utils/
│           ├── ConsoleTable.java
│           └── InputUtil.java
├── docs/
│   ├── class-diagram.puml
│   └── manual-test-checklist.md
├── reports/
├── README.md
├── RUN_SMARTBANK.bat
└── run-smartbank.sh
```

### What each part is responsible for

- `models`: account types, account status and transaction data.
- `services`: account creation, transfers and statement generation.
- `utils`: input reading and aligned console tables.
- `main`: the menu that connects the user to the banking operations.
- `docs`: the editable PlantUML class diagram.
- `reports`: CSV statements created when the export option is used.

The menu is kept separate from the account rules. That way, changing the way a user interacts with the program does not require rewriting the account classes.

## Requirements

- JDK 11 or later
- Command Prompt, PowerShell, Terminal, or an IDE such as VS Code / IntelliJ IDEA

Check that Java is installed:

```bash
java -version
javac -version
```

## Run the program

### Windows

Open Command Prompt in the `SmartBank` folder and run:

```bat
RUN_SMARTBANK.bat
```

Or compile and run manually:

```bat
if not exist out mkdir out
javac -encoding UTF-8 -d out src\com\smartbank\models\*.java src\com\smartbank\services\*.java src\com\smartbank\utils\*.java src\com\smartbank\main\Main.java
java -cp out com.smartbank.main.Main
```

### macOS / Linux

```bash
chmod +x run-smartbank.sh
./run-smartbank.sh
```

Or compile manually:

```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out com.smartbank.main.Main
```

## Try the sample accounts

Two sample accounts are created whenever the program starts:

| Account number | Name | Type | Starting balance | Rule |
|---|---|---|---:|---|
| 100001 | Aarav Sharma | Savings | ₹10,000.00 | Minimum balance ₹2,000.00 |
| 100002 | Neha Verma | Current | ₹25,000.00 | Overdraft limit ₹10,000.00 |

These are only sample records to make the first run easier to test. They are not saved between runs.

## Menu options

| Option | Action |
|---:|---|
| 1 | Create Savings Account |
| 2 | Create Current Account |
| 3 | View All Accounts |
| 4 | Search Account |
| 5 | Deposit Money |
| 6 | Withdraw Money |
| 7 | Transfer Money |
| 8 | Check Account Balance |
| 9 | Transaction History |
| 10 | Generate Account Statement |
| 11 | Block / Unblock / Close Account |
| 12 | Banking Statistics |
| 13 | Export Statement (CSV) |
| 0 | Exit |

## A simple test run

You can use the two sample accounts to try a transfer:

1. Choose option `7`.
2. Enter `100001` as the source account.
3. Enter `100002` as the destination account.
4. Enter an amount such as `500`.
5. Use option `3` to check both balances.
6. Use option `9` with either account number to view its transaction history.

The sender should have ₹500 less and the receiver should have ₹500 more. Both accounts should show a transfer entry in their history.

## Checks to try before submitting

| Test | Expected result |
|---|---|
| Open a Savings Account with an opening balance below its minimum | Account creation is rejected |
| Deposit a positive amount | Balance increases |
| Deposit zero or a negative amount | Request is rejected |
| Withdraw while keeping the Savings minimum | Withdrawal succeeds |
| Withdraw so the Savings balance would fall below its minimum | Request is rejected |
| Use more than the Current Account overdraft limit | Request is rejected |
| Transfer between two active accounts | Both balances and histories are updated |
| Transfer to an unknown account | Request is rejected without debiting the sender |
| Transfer to the same account | Request is rejected |
| Try a transaction on a blocked or closed account | Request is rejected |
| Close an account with a non-zero balance | Request is rejected |
| Export a statement | A CSV file is written under `reports/` |

## UML diagram

The class diagram source is in `docs/class-diagram.puml`. It shows the inheritance relationship between `BankAccount`, `SavingsAccount` and `CurrentAccount`, as well as how the bank, transactions and statement service relate to one another.

To view it, install a PlantUML extension in VS Code or open the `.puml` file with a PlantUML renderer. Keep the `.puml` source file with the submission so the diagram can be edited later.

## Current limitations

- Accounts and transactions are stored in memory. New records disappear when the program exits.
- The two sample accounts are recreated at every launch.
- CSV files exported by the user remain in the `reports` folder.
- There is no database, login system, GUI or connection to a real bank.
- The program is intended for demonstrating Java and OOP concepts, not for handling real customer funds.

## Possible next improvements

If the project is extended later, useful next steps would be persistent storage, automated unit tests, customer authentication and a more detailed statement summary.


## JDBC Integration (Task 3)

See [`README_JDBC.md`](README_JDBC.md) for MySQL setup, JDBC configuration, DAO CRUD implementation, and verification commands.
