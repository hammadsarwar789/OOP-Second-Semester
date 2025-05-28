package Art;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final String USERS_FILE = "users.txt";
    private Map<String, User> users = new HashMap<>();

    public UserManager() {
        loadUsers();
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void addUser(String email, String password, String role) {
        users.put(email.toLowerCase(), new User(email, password, role));
        saveUsers();
    }

    public void removeUser(String email) {
        users.remove(email.toLowerCase());
        saveUsers();
    }

    private void loadUsers() {
        users.clear();
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            users.put("admin@admin.com", new User("admin@admin.com", "admin123", "admin"));
            users.put("seller@seller.com", new User("seller@seller.com", "seller123", "seller"));
            users.put("buyer@buyer.com", new User("buyer@buyer.com", "buyer123", "buyer"));
            saveUsers();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.put(parts[0].toLowerCase(), new User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users.values()) {
                writer.write(user.getEmail() + "," + user.getPassword() + "," + user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
}