package ATM;

import java.util.Scanner;

public class ATMMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ATM System ===");
        System.out.println("Note: Account numbers must be exactly 6 digits");

        int accountNumber = 0;
        boolean validAccount = false;

        while (!validAccount) {
            try {
                System.out.print("Enter account number: ");
                accountNumber = scanner.nextInt();

                String accountStr = String.valueOf(accountNumber);
                if (accountStr.length() != 6) {
                    System.out.println(" Error: Account number must be exactly 6 digits!");
                    System.out.println("try again!");
                    continue;
                }

                if (Databaseconnection.accountExists(accountNumber)) {
                    validAccount = true;
                    System.out.println(" Account found! Loading your account...");
                } else {
                    System.out.println(" Error: Account number " + accountNumber + " not found in database!");
                    System.out.println("try again!");
                }

            } catch (Exception e) {
                System.out.println(" Error: Please enter a valid numeric account number!");
                scanner.next();
            }
        }
        Account account = new Account(accountNumber);

        if (account.isValidAccount()) {
            String accountType = "Savings";
            try {
                Savings_Account savingsAccount = new Savings_Account(accountNumber, account.getBalance(), 0.5);
                ATM atm = new ATM(savingsAccount);
                System.out.println("Welcome to " + accountType + " Account");
                atm.run();
            } catch (Exception e) {
                System.out.println("Error loading account details. Please try again.");
                e.printStackTrace();
            }
        } else {
            System.out.println(" Account validation failed. Please contact bank support.");
        }

        scanner.close();
        Databaseconnection.closeConnection();
    }
}