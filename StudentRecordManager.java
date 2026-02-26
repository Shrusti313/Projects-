import java.util.ArrayList;
import java.util.Scanner;

// Helper class to store student data
class Student {
    private String name;
    private int rollNumber;
    private double marks;

    public Student(String name, int rollNumber, double marks) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.marks = marks;
    }

    public String getName() { return name; }
    public int getRollNumber() { return rollNumber; }
    public double getMarks() { return marks; }

    @Override
    public String toString() {
        return "Roll No: " + rollNumber + " | Name: " + name + " | Marks: " + marks;
    }
}

// Main class containing all the logic
public class StudentRecordManager {
    private static ArrayList<Student> students = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Student Record Manager Menu ---");
            System.out.println("1. Add Student");
            System.out.println("2. View All Student Records");
            System.out.println("3. Search Student by Roll Number");
            System.out.println("4. Calculate Average Marks");
            System.out.println("5. Display Topper (Highest Marks)");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the leftover newline character

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewAllRecords();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    calculateAverage();
                    break;
                case 5:
                    displayTopper();
                    break;
                case 6:
                    exit = true;
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please select 1-6.");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Roll Number: ");
        int roll = scanner.nextInt();
        System.out.print("Enter Marks: ");
        double marks = scanner.nextDouble();
        
        students.add(new Student(name, roll, marks));
        System.out.println("Record added successfully!");
    }

    private static void viewAllRecords() {
        if (students.isEmpty()) {
            System.out.println("No records found.");
        } else {
            System.out.println("\n--- All Student Records ---");
            for (Student s : students) {
                System.out.println(s);
            }
        }
    }

    private static void searchStudent() {
        System.out.print("Enter Roll Number to search: ");
        int roll = scanner.nextInt();
        for (Student s : students) {
            if (s.getRollNumber() == roll) {
                System.out.println("Result: " + s);
                return;
            }
        }
        System.out.println("No student found with Roll Number: " + roll);
    }

    private static void calculateAverage() {
        if (students.isEmpty()) {
            System.out.println("List is empty. Cannot calculate average.");
            return;
        }
        double sum = 0;
        for (Student s : students) {
            sum += s.getMarks();
        }
        System.out.println("The average marks of all students: " + (sum / students.size()));
    }

    private static void displayTopper() {
        if (students.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        Student topper = students.get(0);
        for (Student s : students) {
            if (s.getMarks() > topper.getMarks()) {
                topper = s;
            }
        }
        System.out.println("The Topper is: " + topper);
    }
}