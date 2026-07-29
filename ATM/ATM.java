package ATM;

import java.util.Scanner;

public class ATM {
    private Account account;
    private Scanner scanner;

    public ATM(Account account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("\n=== ATM Machine ===");
        System.out.println("[1] Check Balance");
        System.out.println("[2] Withdraw");
        System.out.println("[3] Deposit");
        System.out.println("[4] View Transaction History");
        System.out.println("[5] Exit");
        System.out.println("==================");
    }

    public void run() {
        while (true) {
            displayMenu();
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
            case 1:
                System.out.printf("Balance: $%.2f%n", account.getBalance());
                account.logTransaction("Balance Inquiry", 0);
                break;
            case 2:
                System.out.print("Enter withdrawal amount: $");
                double withdrawAmount = scanner.nextDouble();
                if (withdrawAmount <= 0) {
                    System.out.println("Invalid amount!");
                } else if (account.withdraw(withdrawAmount)) {
                    System.out.printf("Withdrawal successful! New balance: $%.2f%n", account.getBalance());
                } else {
                    System.out.println("Withdrawal failed: Insufficient funds!");
                }
                break;
            case 3:
                System.out.print("Enter deposit amount: $");
                double depositAmount = scanner.nextDouble();
                if (depositAmount <= 0) {
                    System.out.println("Invalid amount!");
                } else {
                    if (account.deposit(depositAmount)) {
                        System.out.printf("New balance: $%.2f%n", account.getBalance());
                    }
                }
                break;
            case 4:
                account.viewTransactionHistory();
                break;
            case 5:
                System.out.println("Thank you for using our ATM!");
                Databaseconnection.closeConnection();
                return;
            default:
                System.out.println("Invalid choice! Please try again.");
                break;
            }
        }
    }
}