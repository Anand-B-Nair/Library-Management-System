import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddBook extends JFrame {
    private JTextField titleField, authorField;
    private JButton addButton;

    public AddBook() {
        setTitle("Add New Book");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        // UI Components
        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        authorField = new JTextField();
        addButton = new JButton("Add Book");

        // Add components
        add(titleLabel);
        add(titleField);
        add(authorLabel);
        add(authorField);
        add(new JLabel()); // Empty space
        add(addButton);

        // Add Button Event
        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();

            if (!title.isEmpty() && !author.isEmpty()) {
                addBookToDatabase(title, author);
            } else {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addBookToDatabase(String title, String author) {
        String query = "INSERT INTO books (title, author, available) VALUES (?, ?, TRUE)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

