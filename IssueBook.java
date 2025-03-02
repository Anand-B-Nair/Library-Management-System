import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class IssueBook extends JFrame {
    private JTextField bookIdField, userIdField;
    private JButton issueButton;

    public IssueBook() {
        setTitle("Issue Book");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        // UI Components
        JLabel bookLabel = new JLabel("Book ID:");
        bookIdField = new JTextField();
        JLabel userLabel = new JLabel("User ID:");
        userIdField = new JTextField();
        issueButton = new JButton("Issue Book");

        // Add components
        add(bookLabel);
        add(bookIdField);
        add(userLabel);
        add(userIdField);
        add(new JLabel()); // Empty space
        add(issueButton);

        // Button Click Event
        issueButton.addActionListener(e -> {
            int bookId = Integer.parseInt(bookIdField.getText());
            int userId = Integer.parseInt(userIdField.getText());
            issueBookToUser(bookId, userId);
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void issueBookToUser(int bookId, int userId) {
        String checkQuery = "SELECT available FROM books WHERE id = ?";
        String issueQuery = "INSERT INTO issued_books (book_id, user_id, issue_date, return_date) VALUES (?, ?, CURDATE(), NULL)";
        String updateQuery = "UPDATE books SET available = available - 1 WHERE id = ? AND available > 0";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement issueStmt = conn.prepareStatement(issueQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            // Check if the book is available
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getBoolean("available")) {
                // Issue book
                issueStmt.setInt(1, bookId);
                issueStmt.setInt(2, userId);
                issueStmt.executeUpdate();

                // Update book availability
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book issued successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Book is not available!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error issuing book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

