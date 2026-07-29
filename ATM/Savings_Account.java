package ATM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Savings_Account extends Account {
    private double interest_rate;

    public Savings_Account(int accountNumber, double balance, double interestRate) {
        super(accountNumber, balance);
        this.interest_rate = interestRate;

        if (Databaseconnection.accountExists(accountNumber)) {
            System.out.println("Account already exists! Loading existing account.");
            loadInterestRateFromDatabase();
            loadFromDatabase();
        } else {
            System.out.println("Creating new Savings Account...");
            saveToDatabase();
        }
    }

    public Savings_Account(int accountNumber) {
        super(accountNumber);
        if (isValidAccount()) {
            loadInterestRateFromDatabase();
        }
    }

    @Override
    protected void loadFromDatabase() {
        String query = "SELECT balance, account_type FROM accounts WHERE account_number = ?";
        try (Connection conn = Databaseconnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, getAccountNumber());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                setBalance(rs.getDouble("balance"));
                setValidAccount(true);
                System.out.println("Account loaded from database!");
            } else {
                setValidAccount(false);
                System.err.println("Account not found in database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            setValidAccount(false);
        }
    }

    private void saveToDatabase() {
        String query = "INSERT INTO accounts (account_number, balance, interest_rate, account_type) VALUES (?, ?, ?, 'Savings')";

        try (Connection conn = Databaseconnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, getAccountNumber());
            pstmt.setDouble(2, getBalance());
            pstmt.setDouble(3, interest_rate);
            pstmt.executeUpdate();

            setValidAccount(true);
            System.out.println("New Savings Account created successfully!");

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                System.err.println("Account already exists!");
            } else {
                e.printStackTrace();
            }
            setValidAccount(false);
        }
    }

    private void loadInterestRateFromDatabase() {
        String query = "SELECT interest_rate FROM accounts WHERE account_number = ?";
        try (Connection conn = Databaseconnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, getAccountNumber());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.interest_rate = rs.getDouble("interest_rate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getInterestRate() {
        return this.interest_rate;
    }

    public void setInterestRate(double rate) {
        this.interest_rate = rate;
        updateInterestRateInDatabase();
    }

    private void updateInterestRateInDatabase() {
        String query = "UPDATE accounts SET interest_rate = ? WHERE account_number = ?";
        try (Connection conn = Databaseconnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, interest_rate);
            pstmt.setInt(2, getAccountNumber());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}