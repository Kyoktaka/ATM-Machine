package ATM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
    private int accountNumber;
    private double balance;
    private String accountType;
    private boolean isValidAccount = false;

    public Account(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Account(int accountNumber) {
        this.accountNumber = accountNumber;
        loadFromDatabase();
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public boolean isValidAccount() {
        return isValidAccount;
    }

    private void loadFromDatabase() {
        String query = "SELECT balance, account_type FROM accounts WHERE account_number = ?";
        try (Connection conn = Databaseconnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.balance = rs.getDouble("balance");
                this.accountType = rs.getString("account_type");
                this.isValidAccount = true;
            } else {
                this.isValidAccount = false;
                System.err.println("Account not found in database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            this.isValidAccount = false;
        }
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double amount) {
        this.balance = amount;
        updateBalanceInDatabase();
    }

    private void updateBalanceInDatabase() {
        String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = Databaseconnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, balance);
            pstmt.setInt(2, accountNumber);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deposit(double amount) {
        if (!isValidAccount) {
            System.out.println("Error: Invalid account!");
            return;
        }
        balance += amount;
        updateBalanceInDatabase();
        logTransaction("Deposit", amount);
    }

    public boolean withdraw(double amount) {
        if (!isValidAccount) {
            System.out.println("Error: Invalid account!");
            return false;
        }
        if (amount <= balance) {
            balance -= amount;
            updateBalanceInDatabase();
            logTransaction("Withdrawal", amount);
            return true;
        }
        return false;
    }

    public void logTransaction(String type, double amount) {
        if (!isValidAccount)
            return;

        String query = "INSERT INTO transactions (account_number, transaction_type, amount) VALUES (?, ?, ?)";
        try (Connection conn = Databaseconnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, accountNumber);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewTransactionHistory() {
        if (!isValidAccount) {
            System.out.println("Error: Invalid account!");
            return;
        }

        String query = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";
        try (Connection conn = Databaseconnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n=== Transaction History ===");
            boolean hasTransactions = false;
            while (rs.next()) {
                hasTransactions = true;
                System.out.printf("ID: %d | Type: %s | Amount: $%.2f | Date: %s%n", rs.getInt("transaction_id"),
                        rs.getString("transaction_type"), rs.getDouble("amount"), rs.getTimestamp("transaction_date"));
            }
            if (!hasTransactions) {
                System.out.println("No transactions found.");
            }
            System.out.println("===========================\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}