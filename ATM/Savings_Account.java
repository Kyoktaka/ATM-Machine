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
        saveToDatabase();
    }

    public Savings_Account(int accountNumber) {
        super(accountNumber);
        loadInterestRateFromDatabase();
    }

    private void saveToDatabase() {
        String checkQuery = "SELECT * FROM accounts WHERE account_number = ?";
        String insertQuery = "INSERT INTO accounts (account_number, balance, interest_rate, account_type) VALUES (?, ?, ?, 'Savings')";
        String updateQuery = "UPDATE accounts SET balance = ?, interest_rate = ? WHERE account_number = ?";

        try (Connection conn = Databaseconnection.getConnection()) {
          
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, getAccountNumber());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
           
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setDouble(1, getBalance());
                updateStmt.setDouble(2, interest_rate);
                updateStmt.setInt(3, getAccountNumber());
                updateStmt.executeUpdate();
            } else {
             
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
