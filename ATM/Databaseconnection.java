package ATM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Databaseconnection {
    private static final String URL = "jdbc:mysql://localhost:3306/atm_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("Database connection closed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean accountExists(int accountNumber) {
        String query = "SELECT COUNT(*) FROM accounts WHERE account_number = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void printAllAccounts() {
        String query = "SELECT * FROM accounts";
        try (Connection conn = getConnection();
                java.sql.Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n=== All Accounts in Database ===");
            while (rs.next()) {
                System.out.printf("Account: %d | Balance: $%.2f | Type: %s | Rate: %.2f%%%n",
                        rs.getInt("account_number"), rs.getDouble("balance"), rs.getString("account_type"),
                        rs.getDouble("interest_rate"));
            }
            System.out.println("================================\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
