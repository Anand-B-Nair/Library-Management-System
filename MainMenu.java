import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private JButton viewBooks, addBook, issueBook, returnBook;

    public MainMenu() {
        setTitle("Library Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        viewBooks = new JButton("View Books");
        addBook = new JButton("Add Book");
        issueBook = new JButton("Issue Book");
        returnBook = new JButton("Return Book");

        add(viewBooks);
        add(addBook);
        add(issueBook);
        add(returnBook);

        // Event Listeners
        viewBooks.addActionListener(e -> new ViewBooks());
        addBook.addActionListener(e -> new AddBook());
        issueBook.addActionListener(e -> new IssueBook());
        returnBook.addActionListener(e -> new ReturnBook());

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
