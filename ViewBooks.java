import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;

public class ViewBooks extends JFrame {
    JTable table;
    DefaultTableModel model;

    public ViewBooks() {
        setTitle("View Books");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create table
        model = new DefaultTableModel();
        table = new JTable(model);

        // Set table headers based on your database
        model.addColumn("Book ID");
        model.addColumn("Title");
        model.addColumn("Author");
        model.addColumn("Availability");

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Customize table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));

        // Load books from database
        loadBooks();

        setVisible(true);
    }

    private void loadBooks() {
        try {
            // Database connection
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_db", "root", "mysqlonhp@2025");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, title, author, available FROM books");

            // Clear existing data
            model.setRowCount(0);

            // Add rows to table
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("available")
                });
            }

            con.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewBooks::new);
    }
}

