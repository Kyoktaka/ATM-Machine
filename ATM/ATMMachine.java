package ATM;

import java.util.Scanner;

public class ATMMachine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ATM System ===");
        System.out.print("Enter account number: ");
        int accountNumber = scanner.nextInt();

        Savings_Account savingsAccount = new Savings_Account(accountNumber, 10000.0, 0.5);
        ATM atm = new ATM(savingsAccount);
        System.out.println("Welcome to Savings Account");
        atm.run();

        scanner.close();
    }
}