import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PasswordManager {
    private Map<String, String> passwords;
    private static final int MIN_PASSWORD_STRENGTH = 8; // Minimum password strength threshold
    private static final int EXPIRATION_MONTHS = 2; // Expiration period in months
    private String loggedInUserId;

    public PasswordManager() {
        passwords = new HashMap<>();
        loggedInUserId = null;
    }

    public void addPassword(String website, String password) {
        int strength = getPasswordStrength(password);
        if (strength >= MIN_PASSWORD_STRENGTH) {
            LocalDate expirationDate = LocalDate.now().plusMonths(EXPIRATION_MONTHS);
            passwords.put(website, password);
            System.out.println("Password added for " + website + " with expiration date: " + expirationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else {
            System.out.println("Warning: Password does not meet strong criteria. Password strength: " + strength);
            System.out.println("Please try again with a stronger password.");
        }
    }

    public void updatePassword(String website, String newPassword) {
        int strength = getPasswordStrength(newPassword);
        if (strength >= MIN_PASSWORD_STRENGTH) {
            if (passwords.containsKey(website)) {
                LocalDate expirationDate = LocalDate.now().plusMonths(EXPIRATION_MONTHS);
                passwords.put(website, newPassword);
                System.out.println("Password updated for " + website + " with expiration date: " + expirationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } else {
                System.out.println("No password found for " + website);
            }
        } else {
            System.out.println("Warning: New password does not meet strong criteria. Password strength: " + strength);
            System.out.println("Password not updated. Please try again with a stronger password.");
        }
    }

    private int getPasswordStrength(String password) {
        // Calculate password strength
        int strength = 0;
        if (password.length() >= 8) strength += 2;
        if (password.matches(".*[A-Z].*")) strength += 2;
        if (password.matches(".*[a-z].*")) strength += 2;
        if (password.matches(".*\\d.*")) strength += 2;
        if (password.matches(".*[!@#$%^&*()-+=_].*")) strength += 2;
        return strength;
    }

    public String getPassword(String website) {
        if (passwords.containsKey(website)) {
            return passwords.get(website);
        } else {
            return "No password found for " + website;
        }
    }

    public void deletePassword(String website) {
        if (passwords.containsKey(website)) {
            passwords.remove(website);
            System.out.println("Password deleted for " + website);
        } else {
            System.out.println("No password found for " + website);
        }
    }

    public void displayAllPasswords() {
        for (Map.Entry<String, String> entry : passwords.entrySet()) {
            System.out.println("Website: " + entry.getKey() + ", Password: " + entry.getValue());
        }
    }

    public void login(String userId, String password) {
        if (loggedInUserId != null) {
            System.out.println("Already logged in as user: " + loggedInUserId);
            return;
        }
        // Perform authentication
        if (authenticate(userId, password)) {
            loggedInUserId = userId;
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed. Incorrect user ID or password.");
        }
    }

    public void logout() {
        if (loggedInUserId == null) {
            System.out.println("Already logged out.");
        } else {
            loggedInUserId = null;
            System.out.println("Logged out successfully!");
        }
    }

    boolean authenticate(String userId, String password) {
  
        // Implement secure authentication logic here
        // For simplicity, let's assume the password is stored in a secure database and retrieved for comparison
        // Here, we'll use a simple in-memory map to store the hashed passwords
        Map<String, String> secureDatabase = new HashMap<>();
        secureDatabase.put("harshithatr", hashPassword("Hari@123"));
        secureDatabase.put("sowmyak", hashPassword("sow@1234"));

        // Retrieve the hashed password for the given user ID from the secure database
        String hashedPassword = secureDatabase.getOrDefault(userId, "");

        // Hash the input password and compare it with the hashed password stored in the secure database
        return hashPassword(password).equals(hashedPassword);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        PasswordManager passwordManager = new PasswordManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (passwordManager.loggedInUserId == null) {
                System.out.println("Please log in to continue.");
                System.out.print("Enter your user ID: ");
                String userId = scanner.next();
                System.out.print("Enter your password: ");
                String password = scanner.next();
                passwordManager.login(userId, password);
                continue;
            }

            System.out.println("1. Add Password");
            System.out.println("2. Update Password");
            System.out.println("3. Get Password");
            System.out.println("4. Delete Password");
            System.out.println("5. Display All Passwords");
            System.out.println("6. Logout");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter website: ");
                    String website = scanner.next();
                    System.out.print("Enter password: ");
                    String pass = scanner.next();
                    passwordManager.addPassword(website, pass);
                    break;
                case 2:
                    System.out.print("Enter website: ");
                    String websiteToUpdate = scanner.next();
                    System.out.print("Enter new password: ");
                    String newPassword = scanner.next();
                    passwordManager.updatePassword(websiteToUpdate, newPassword);
                    break;
                case 3:
                    System.out.print("Enter website: ");
                    String websiteToGet = scanner.next();
                    System.out.println("Password: " + passwordManager.getPassword(websiteToGet));
                    break;
                case 4:
                    System.out.print("Enter website to delete: ");
                    String websiteToDelete = scanner.next();
                    passwordManager.deletePassword(websiteToDelete);
                    break;
                case 5:
                    passwordManager.displayAllPasswords();
                    break;
                case 6:
                    passwordManager.logout();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
