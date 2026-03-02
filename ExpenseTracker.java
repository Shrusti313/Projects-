import java.util.ArrayList;
import java.util.Scanner;

class Expense {
    String description;
    double amount;

    public Expense(String description, double amount) {
        this.description = description;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return description + ": $" + amount;
    }
}

public class ExpenseTracker {
    private static ArrayList<Expense> expenses = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--- Welcome to Learn Depth Expense Tracker ---");
        
        while (true) {
            System.out.println("\n1. Add Expense\n2. View All Expenses\n3. Calculate Total\n4. Delete Expense\n5. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: addExpense(); break;
                case 2: viewExpenses(); break;
                case 3: calculateTotal(); break;
                case 4: deleteExpense(); break;
                case 5: 
                    System.out.println("Exiting... Happy Budgeting!");
                    return;
                default: 
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addExpense() {
        System.out.print("Enter Expense (e.g., Food): ");
        String desc = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        
        expenses.add(new Expense(desc, amount));
        System.out.println("Expense Added Successfully");
        calculateTotal();
    }

    private static void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.println("\n--- Your Expenses ---");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }
    }

    private static void calculateTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.amount;
        }
        System.out.println("Total Expenses: " + total);
    }

    private static void deleteExpense() {
        viewExpenses();
        if (expenses.isEmpty()) return;
        
        System.out.print("Enter the number of the expense to delete: ");
        int index = scanner.nextInt() - 1;
        
        if (index >= 0 && index < expenses.size()) {
            expenses.remove(index);
            System.out.println("Expense deleted successfully.");
        } else {
            System.out.println("Invalid index.");
        }
    }
}