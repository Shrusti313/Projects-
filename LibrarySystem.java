import java.util.ArrayList;
import java.util.Scanner;

// Class to represent a Book
class Book {
    int bookId;
    String bookName;
    String authorName;
    boolean isIssued;

    // Constructor to initialize book details
    public Book(int id, String name, String author) {
        this.bookId = id;
        this.bookName = name;
        this.authorName = author;
        this.isIssued = false; 
    }
}

public class LibrarySystem {
    public static void main(String[] args) {
        ArrayList<Book> bookList = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        int choice;

        System.out.println("***** Library Management System *****");

        do {
            System.out.println("\n1. Add New Book");
            System.out.println("2. View All Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Enter Choice: ");
            choice = input.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Book ID: ");
                    int id = input.nextInt();
                    input.nextLine(); // Clear buffer
                    System.out.print("Enter Book Name: ");
                    String name = input.nextLine();
                    System.out.print("Enter Author Name: ");
                    String author = input.nextLine();
                    
                    Book newBook = new Book(id, name, author);
                    bookList.add(newBook);
                    System.out.println("Success: Book added to records.");
                    break;

                case 2:
                    if (bookList.isEmpty()) {
                        System.out.println("The library is currently empty.");
                    } else {
                        System.out.println("\n--- Current Inventory ---");
                        for (Book b : bookList) {
                            String status = b.isIssued ? "[Issued]" : "[Available]";
                            System.out.println("ID: " + b.bookId + " | Name: " + b.bookName + 
                                             " | Author: " + b.authorName + " | Status: " + status);
                        }
                    }
                    break;

                case 3:
                    System.out.print("Enter Book ID to issue: ");
                    int issueId = input.nextInt();
                    boolean issueFound = false;
                    for (Book b : bookList) {
                        if (b.bookId == issueId) {
                            issueFound = true;
                            if (!b.isIssued) {
                                b.isIssued = true;
                                System.out.println("Book '" + b.bookName + "' issued successfully.");
                            } else {
                                System.out.println("Error: This book is already out on loan.");
                            }
                            break;
                        }
                    }
                    if (!issueFound) System.out.println("Error: Book ID not found.");
                    break;

                case 4:
                    System.out.print("Enter Book ID to return: ");
                    int returnId = input.nextInt();
                    boolean returnFound = false;
                    for (Book b : bookList) {
                        if (b.bookId == returnId) {
                            returnFound = true;
                            if (b.isIssued) {
                                b.isIssued = false;
                                System.out.println("Book '" + b.bookName + "' returned successfully.");
                            } else {
                                System.out.println("Error: This book was not marked as issued.");
                            }
                            break;
                        }
                    }
                    if (!returnFound) System.out.println("Error: Book ID not found.");
                    break;

                case 5:
                    System.out.println("System shutting down... Goodbye!");
                    break;

                default:
                    System.out.println("Invalid input. Please choose 1-5.");
            }
        } while (choice != 5);

        input.close();
    }
}