package ATM;

import java.util.Scanner;

public class ATMMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ATM System ===");
        System.out.println("Note: Account numbers must be exactly 6 digits");

        int accountNumber = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Enter account number: ");
                accountNumber = scanner.nextInt();

                String accountStr = String.valueOf(accountNumber);
                if (accountStr.length() != 6) {
                    System.out.println(" Error: Account number must be exactly 6 digits!");
                    System.out.println("try again!");
                    continue;
                }

                validInput = true;

            } catch (Exception e) {
                System.out.println(" Error: Please enter a valid number!");
                scanner.next();
            }
        }

        if (Databaseconnection.accountExists(accountNumber)) {
            System.out.println(" Account found! Loading your account...");

            Account account = new Account(accountNumber);

            if (account.isValidAccount()) {

                Savings_Account savingsAccount = new Savings_Account(accountNumber, account.getBalance(), 0.5);

                ATM atm = new ATM(savingsAccount);
                System.out.println("Welcome to Savings Account #" + accountNumber);
                atm.run();
            } else {
                System.out.println("Error: Could not load account. Please contact support.");
            }
        } else {
            System.out.println("Error: Account number " + accountNumber + " not found!");
            System.out.println("try again!");
        }

        scanner.close();
        Databaseconnection.closeConnection();
    }
}