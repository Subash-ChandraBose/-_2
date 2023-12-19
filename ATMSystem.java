import java.util.ArrayList;
import java.util.Scanner;

class User {
    private String userId;
    private String pin;
    private ArrayList<Account> accounts;

    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
        this.accounts = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
}

class Account {
    private String accountNumber;
    private double balance;
    private ArrayList<String> transactionHistory;

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        recordTransaction("Deposit: +" + amount);
        System.out.println("Account Balance:"+balance);
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            recordTransaction("Withdrawal: -" + amount);
            System.out.println("Withdraw Successfully");
            System.out.println("Account Balance:"+balance);
            

        } else {
            System.out.println("Insufficient funds");
        }
    }

    public void transfer(Account recipient, double amount) {
        if (balance >= amount) {
            balance -= amount;
            recipient.deposit(amount);
            recordTransaction("Transfer to " + recipient.getAccountNumber() + ": -" + amount);
        } else {
            System.out.println("Insufficient funds");
        }
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }

    private void recordTransaction(String transaction) {
        transactionHistory.add(transaction);
    }
}

class ATM {
    private User currentUser;

    public boolean login(String userId, String pin, ArrayList<User> users) {
        for (User user : users) {
            if (user.getUserId().equals(userId) && user.getPin().equals(pin)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void displayMenu() {
        System.out.println("1. Transaction History");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
    }

    public void performOperation(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                displayTransactionHistory();
                break;
            case 2:
                withdraw(scanner);
                break;
            case 3:
                deposit(scanner);
                System.out.println("Deposited Successfully");
                break;
            case 4:
                transfer(scanner);
                break;
            case 5:
                System.out.println("Thank you for using the ATM. Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void displayTransactionHistory() {
        System.out.println("Transaction History:");
        for (String transaction : getCurrentUser().getAccounts().get(0).getTransactionHistory()) {
            System.out.println(transaction);
        }
    }

    private void withdraw(Scanner scanner) {
        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        getCurrentUser().getAccounts().get(0).withdraw(amount);
    }

    private void deposit(Scanner scanner) {
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        getCurrentUser().getAccounts().get(0).deposit(amount);
    }

    private void transfer(Scanner scanner) {
        System.out.print("Enter recipient's account number: ");
        String recipientAccountNumber = scanner.next();
        System.out.print("Enter transfer amount: ");
        double amount = scanner.nextDouble();

        Account recipient = findRecipientAccount(recipientAccountNumber);
        if (recipient != null) {
            getCurrentUser().getAccounts().get(0).transfer(recipient, amount);
        } else {
            System.out.println("Recipient account not found.");
        }
    }

    private Account findRecipientAccount(String accountNumber) {
        for (User user : getAllUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    return account;
                }
            }
        }
        return null;
    }

    private User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        User Subash = new User("Subash2907", "2907");
        Subash.addAccount(new Account("11112222"));
        users.add(Subash);

        User Anand = new User("Anand1211", "1211");
        Anand.addAccount(new Account("33334444"));
        users.add(Anand);

        return users;
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATM atm = new ATM();

        System.out.print("Enter user ID: ");
        String userId = scanner.next();
        System.out.print("Enter PIN: ");
        String pin = scanner.next();

        if (atm.login(userId, pin, atm.getAllUsers())) {
            while (true) {
                atm.displayMenu();
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                atm.performOperation(choice, scanner);
            }
        } else {
            System.out.println("Login failed. Incorrect user ID or PIN.");
        }
    }
}
