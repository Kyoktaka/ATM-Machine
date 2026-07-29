package ATM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Savings_Account extends Account {
    private double interest_rate;
    private boolean isNewAccount = false;

    public Savings_Account(int accountNumber, double balance, double interestRate) {
        super(accountNumber, balance);
        this.interest_rate = interestRate;

        // Check if account exists
        if (Databaseconnection.accountExists(accountNumber)) {
            System.out.println("Account already exists! Loading existing account.");
            loadInterestRateFromDatabase();
        } else {
            System.out.println("Creating new Savings Account...");
            this.isNewAccount = true;
            saveToDatabase();
        }
    }

    public Savings_Account(int accountNumber) {
        super(accountNumber);
        loadInterestRateFromDatabase();
    }

    private void saveToDatabase() {
        String query = "INSERT INTO accounts (account_number, balance, interest_rate, account_type) VALUES (?, ?, ?, 'Savings')";

        try (Connection conn = Databaseconnection.getConnection()) {
            // Check if account exists
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, getAccountNumber());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Update existing account
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setDouble(1, getBalance());
                updateStmt.setDouble(2, interest_rate);
                updateStmt.setInt(3, getAccountNumber());
                updateStmt.executeUpdate();
            } else {
                // Insert new account
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, getAccountNumber());
                insertStmt.setDouble(2, getBalance());
                insertStmt.setDouble(3, interest_rate);
                insertStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
