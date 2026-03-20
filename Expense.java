import java.io.Serializable;

public class Expense implements Serializable {
    private String title, category, date, description;
    private double amount;

    public Expense(String title, String category, double amount, String date, String description) {
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return title + "," + category + "," + amount + "," + date + "," + (description.isEmpty() ? "N/A" : description);
    }
}