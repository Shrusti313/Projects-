import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class ExpenseTrackerApp extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    
    private JTextField titleF, amountF, dateF, budgetF, descF;
    private JComboBox<String> catBox;
    private DefaultTableModel tableModel;
    private JTable expenseTable;
    private AnalysisPanel analysisPanel;
    
    private double totalSpent = 0, monthlyBudget = 0;
    private List<Expense> expenseList = new ArrayList<>();
    private final String FILE = "expenses.csv";

    public ExpenseTrackerApp() {
        setTitle("Elite Expense Tracker v3 - Budget Insights");
        setSize(1150, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // --- NAVIGATION ---
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        nav.setBackground(new Color(30, 39, 46));
        JButton btnAdd = new JButton("➕ Add Expense");
        JButton btnView = new JButton("📊 Budget Analysis");
        nav.add(btnAdd); nav.add(btnView);
        add(nav, BorderLayout.NORTH);

        // --- PAGE 1: ADD (Middle) ---
        JPanel addPage = new JPanel(new GridBagLayout());
        JPanel form = new JPanel(new GridLayout(6, 2, 15, 15));
        form.setBorder(BorderFactory.createTitledBorder("Log New Transaction"));
        
        form.add(new JLabel("Title:")); titleF = new JTextField(20); form.add(titleF);
        form.add(new JLabel("Category:")); 
        catBox = new JComboBox<>(new String[]{"Food", "Travel", "Bills", "Shopping", "Education", "Healthcare", "Entertainment", "Other"}); 
        form.add(catBox);
        form.add(new JLabel("Amount (₹):")); amountF = new JTextField(); form.add(amountF);
        form.add(new JLabel("Date:")); dateF = new JTextField("20-03-2026"); form.add(dateF);
        form.add(new JLabel("Description:")); descF = new JTextField(); form.add(descF);
        
        JButton saveBtn = new JButton("Save Record");
        saveBtn.setBackground(new Color(46, 204, 113));
        saveBtn.setForeground(Color.WHITE);
        form.add(new JLabel("")); form.add(saveBtn);
        addPage.add(form);

        // --- PAGE 2: ANALYSIS & VIEW ---
        JPanel viewPage = new JPanel(new BorderLayout(20, 20));
        tableModel = new DefaultTableModel(new String[]{"Title", "Category", "Amt", "Date", "Notes"}, 0);
        expenseTable = new JTable(tableModel);
        expenseTable.setRowHeight(25);
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(expenseTable), BorderLayout.CENTER);
        
        JButton delBtn = new JButton("🗑 Delete Selected");
        delBtn.setBackground(new Color(231, 76, 60));
        delBtn.setForeground(Color.WHITE);
        budgetF = new JTextField(10);
        JButton setBtn = new JButton("Set Limit");
        
        JPanel bottomCtrl = new JPanel();
        bottomCtrl.add(delBtn); bottomCtrl.add(new JLabel("Monthly Budget: ₹")); bottomCtrl.add(budgetF); bottomCtrl.add(setBtn);
        leftPanel.add(bottomCtrl, BorderLayout.SOUTH);

        analysisPanel = new AnalysisPanel();
        analysisPanel.setPreferredSize(new Dimension(500, 400));

        viewPage.add(leftPanel, BorderLayout.CENTER);
        viewPage.add(analysisPanel, BorderLayout.EAST);

        mainPanel.add(addPage, "ADD");
        mainPanel.add(viewPage, "VIEW");
        add(mainPanel, BorderLayout.CENTER);

        // --- ACTION LISTENERS ---
        btnAdd.addActionListener(e -> cardLayout.show(mainPanel, "ADD"));
        btnView.addActionListener(e -> { analysisPanel.repaint(); cardLayout.show(mainPanel, "VIEW"); });
        saveBtn.addActionListener(e -> handleSave());
        delBtn.addActionListener(e -> handleDelete());
        setBtn.addActionListener(e -> {
            try {
                monthlyBudget = Double.parseDouble(budgetF.getText());
                checkBudgetAlert();
                analysisPanel.repaint();
            } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Enter a valid number!"); }
        });

        loadData();
        setVisible(true);
    }

    private void handleSave() {
        try {
            double amt = Double.parseDouble(amountF.getText());
            Expense e = new Expense(titleF.getText(), (String)catBox.getSelectedItem(), amt, dateF.getText(), descF.getText());
            expenseList.add(e);
            tableModel.addRow(new Object[]{e.getTitle(), e.getCategory(), e.getAmount(), e.getDate(), e.getDescription()});
            saveToFile();
            checkBudgetAlert();
            titleF.setText(""); amountF.setText(""); descF.setText("");
            JOptionPane.showMessageDialog(this, "Expense Saved Successfully!");
        } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Invalid Entry!"); }
    }

    private void checkBudgetAlert() {
        totalSpent = expenseList.stream().mapToDouble(Expense::getAmount).sum();
        if (monthlyBudget > 0 && totalSpent > monthlyBudget) {
            JOptionPane.showMessageDialog(this, "🚨 BUDGET EXCEEDED!\nExtra Spent: ₹" + (totalSpent - monthlyBudget), "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleDelete() {
        int row = expenseTable.getSelectedRow();
        if (row != -1) {
            expenseList.remove(row);
            tableModel.removeRow(row);
            saveToFile();
            analysisPanel.repaint();
        }
    }

    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            for (Expense e : expenseList) pw.println(e.toString());
        } catch (IOException e) { }
    }

    private void loadData() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if(d.length < 5) continue;
                expenseList.add(new Expense(d[0], d[1], Double.parseDouble(d[2]), d[3], d[4]));
                tableModel.addRow(d);
            }
        } catch (Exception e) { }
    }

    // --- CUSTOM ANALYSIS DESIGN ---
    class AnalysisPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Map<String, Double> data = new LinkedHashMap<>(); // Maintain order
            totalSpent = 0;
            for (Expense e : expenseList) {
                data.put(e.getCategory(), data.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
                totalSpent += e.getAmount();
            }

            // Heading
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
            g2.drawString("Visual Spending Report", 20, 40);

            // Bar Chart Configuration
            int startX = 130; // Category labels on the left
            int startY = 80;
            int barHeight = 25;
            int maxBarWidth = 250;
            double maxVal = data.values().stream().max(Double::compare).orElse(100.0);

            int i = 0;
            for (String cat : data.keySet()) {
                int width = (int) ((data.get(cat) / maxVal) * maxBarWidth);
                
                // Draw Bar
                g2.setColor(new Color(52, 152, 219));
                g2.fillRoundRect(startX, startY + (i * 45), width, barHeight, 8, 8);
                
                // Draw Text
                g2.setColor(new Color(44, 62, 80));
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                g2.drawString(cat, 20, startY + (i * 45) + 18); // SHIFTED LEFT
                g2.drawString("₹" + data.get(cat), startX + width + 10, startY + (i * 45) + 18);
                i++;
            }

            // --- EXTRA SPENDING / BUDGET INSIGHTS ---
            int insightY = 480;
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(20, insightY - 30, 450, insightY - 30);
            
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.setColor(Color.BLACK);
            g2.drawString("Total Expense: ₹" + totalSpent, 20, insightY);

            if (monthlyBudget > 0) {
                double diff = monthlyBudget - totalSpent;
                if (diff < 0) {
                    g2.setColor(new Color(192, 57, 43)); // Dark Red
                    g2.drawString("EXTRA SPENT: ₹" + Math.abs(diff), 20, insightY + 30);
                } else {
                    g2.setColor(new Color(39, 174, 96)); // Dark Green
                    g2.drawString("REMAINING BUDGET: ₹" + diff, 20, insightY + 30);
                }

                // Progress Bar
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRect(20, insightY + 50, 400, 20);
                double ratio = Math.min(totalSpent / monthlyBudget, 1.0);
                g2.setColor(totalSpent > monthlyBudget ? new Color(231, 76, 60) : new Color(46, 204, 113));
                g2.fillRect(21, insightY + 51, (int)(398 * ratio), 18);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseTrackerApp::new);
    }
}