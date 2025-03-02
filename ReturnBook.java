import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReturnBook extends JFrame {
    private JTextField bookIdField;
    private JButton returnButton;

    public ReturnBook() {
        setTitle("Return Book");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(2, 2));

        // UI Components
        JLabel bookLabel = new JLabel("Book ID:");
        bookIdField = new JTextField();
        returnButton = new JButton("Return Book");

        // Add components
        add(bookLabel);
        add(bookIdField);
        add(new JLabel()); // Empty space
        add(returnButton);

        // Button Click Event
        returnButton.addActionListener(e -> {
            int bookId = Integer.parseInt(bookIdField.getText());
            returnBookToLibrary(bookId);
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void returnBookToLibrary(int bookId) {
        String checkQuery = "SELECT * FROM issued_books WHERE book_id = ? AND return_date IS NULL";
        String returnQuery = "UPDATE issued_books SET return_date = CURDATE() WHERE book_id = ?";
        String updateQuery = "UPDATE books SET available = TRUE WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement returnStmt = conn.prepareStatement(returnQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Mark book as returned
                returnStmt.setInt(1, bookId);
                returnStmt.executeUpdate();

                // Update book availability
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No active issue record found for this book!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error returning book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
