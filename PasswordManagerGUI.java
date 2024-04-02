import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class PasswordManagerGUI {
    private PasswordManager passwordManager;
    private JFrame mainFrame;
    private JTextField userIdField;
    private JPasswordField passwordField;

    public PasswordManagerGUI() {
        passwordManager = new PasswordManager();
    }

    public void createAndShowGUI() {
        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("Password Manager");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(400, 200);
            mainFrame.setLayout(new GridLayout(3, 2));

            JLabel userIdLabel = new JLabel("User ID:");
            userIdField = new JTextField();
            JLabel passwordLabel = new JLabel("Password:");
            passwordField = new JPasswordField();

            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(e -> login());

            mainFrame.add(userIdLabel);
            mainFrame.add(userIdField);
            mainFrame.add(passwordLabel);
            mainFrame.add(passwordField);
            mainFrame.add(loginButton);

            mainFrame.setVisible(true);
        });
    }

    private void login() {
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());

        if (passwordManager.authenticate(userId, password)) {
            JOptionPane.showMessageDialog(mainFrame, "Login successful!");
            openPasswordManagerWindow();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Login failed. Incorrect user ID or password.");
        }
    }

    private void openPasswordManagerWindow() {
        mainFrame.setVisible(false);

        JFrame passwordManagerFrame = new JFrame("Password Manager");
        passwordManagerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        passwordManagerFrame.setSize(600, 400);
        passwordManagerFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel buttonPanel = new JPanel();
        JButton addPasswordButton = new JButton("Add Password");
        addPasswordButton.addActionListener(e -> addPassword(textArea));
        buttonPanel.add(addPasswordButton);

        JButton updatePasswordButton = new JButton("Update Password");
        updatePasswordButton.addActionListener(e -> updatePassword(textArea));
        buttonPanel.add(updatePasswordButton);

        JButton getPasswordButton = new JButton("Get Password");
        getPasswordButton.addActionListener(e -> getPassword(textArea));
        buttonPanel.add(getPasswordButton);

        JButton deletePasswordButton = new JButton("Delete Password");
        deletePasswordButton.addActionListener(e -> deletePassword(textArea));
        buttonPanel.add(deletePasswordButton);

        JButton displayAllPasswordsButton = new JButton("Display All Passwords");
        displayAllPasswordsButton.addActionListener(e -> displayAllPasswords(textArea));
        buttonPanel.add(displayAllPasswordsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout(passwordManagerFrame)); // Pass the passwordManagerFrame
        buttonPanel.add(logoutButton);

        passwordManagerFrame.add(scrollPane, BorderLayout.CENTER);
        passwordManagerFrame.add(buttonPanel, BorderLayout.SOUTH);

        passwordManagerFrame.setVisible(true);
    }

    private void addPassword(JTextArea textArea) {
        String website = JOptionPane.showInputDialog(mainFrame, "Enter website:");
        String password = JOptionPane.showInputDialog(mainFrame, "Enter password:");

        passwordManager.addPassword(website, password);
        textArea.append("Website: " + website + ", Password: " + password + "\n");
    }

    private void updatePassword(JTextArea textArea) {
        String website = JOptionPane.showInputDialog(mainFrame, "Enter website:");
        String newPassword = JOptionPane.showInputDialog(mainFrame, "Enter new password:");

        passwordManager.updatePassword(website, newPassword);
        textArea.setText("");
        displayAllPasswords(textArea);
    }

    private void getPassword(JTextArea textArea) {
        String website = JOptionPane.showInputDialog(mainFrame, "Enter website:");
        String password = passwordManager.getPassword(website);
        textArea.setText("Password for " + website + ": " + password);
    }

    private void deletePassword(JTextArea textArea) {
        String website = JOptionPane.showInputDialog(mainFrame, "Enter website:");
        passwordManager.deletePassword(website);
        textArea.setText("");
        displayAllPasswords(textArea);
    }

    private void displayAllPasswords(JTextArea textArea) {
        Map<String, String> allPasswords = passwordManager.getAllPasswords();
        textArea.setText("");
        for (Map.Entry<String, String> entry : allPasswords.entrySet()) {
            textArea.append("Website: " + entry.getKey() + ", Password: " + entry.getValue() + "\n");
        }
    }

    private void logout(JFrame frame) {
        mainFrame.setVisible(true);
        frame.dispose(); // Close the Password Manager window
    }

    public static void main(String[] args) {
        PasswordManagerGUI gui = new PasswordManagerGUI();
        gui.createAndShowGUI();
    }
}
