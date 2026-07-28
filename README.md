# ATM Banking System

A Java-based ATM (Automated Teller Machine) system with MySQL database integration.

## Features

- **Account Management**: Create and manage Savings and Checking accounts
- **Transactions**: Deposit, Withdraw, and Balance Inquiry
- **Transaction History**: View all transaction records
- **Database Integration**: All data stored in MySQL database
- **Real-time Updates**: Balance updates automatically in database

## Technologies Used

- **Java**: Core application logic
- **MySQL**: Database for storing account and transaction data
- **JDBC**: Database connectivity
- **XAMPP**: Local development environment

## Prerequisites

- Java 8 or higher
- XAMPP (MySQL)
- MySQL Connector/J driver

## Database Setup

1. Start XAMPP and enable MySQL
2. Create database:

```sql
CREATE DATABASE atm_system;
USE atm_system;

CREATE TABLE accounts (
    account_number INT PRIMARY KEY,
    balance DECIMAL(10, 2) NOT NULL,
    interest_rate DECIMAL(5, 2),
    account_type ENUM('Savings', 'Checking') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number INT,
    transaction_type ENUM('Deposit', 'Withdrawal', 'Balance Inquiry') NOT NULL,
    amount DECIMAL(10, 2),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);
```

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/Kyoktaka/ATM-Machine.git
   ```
2. Add MySQL Connector/J to your project's classpath (or via your build tool).
3. Update database credentials in `src/ATM/DatabaseConnection.java`.
4. Compile the Java files:
   ```bash
   javac -d bin src/ATM/*.java
   ```
5. Run the application:
   ```bash
   java -cp "bin:mysql-connector-java.jar" ATM.ATMMachine
   ```

## Project Structure

```text
ATM-Machine/
├── src/
│   └── ATM/
│       ├── Account.java
│       ├── ATM.java
│       ├── ATMMachine.java
│       ├── Checking_Account.java
│       ├── DatabaseConnection.java
│       └── Savings_Account.java
├── .gitignore
├── README.md
└── LICENSE
```

## Usage

1. Enter your account number.
2. Select from the menu options:
   - Check Balance
   - Withdraw
   - Deposit
   - View Transaction History
   - Exit

## Author

- Kyoktaka
