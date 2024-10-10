package application;

import java.util.ArrayList;
import java.util.List;

public class UserTest {
    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nUser Testing Automation");

        // Test cases for User class
        performTestCase(1, new User("testUser", "password123", "admin@example.com"), true); // Valid case
        performTestCase(2, new User("", "password123", "admin@example.com"), false); // Invalid username
        performTestCase(3, new User("testUser", "", "admin@example.com"), false); // Invalid password
        performTestCase(4, new User("testUser", "password123", ""), false); // Invalid email
        performTestCase(5, new User("testUser", "password123", "admin@example.com"), true); // Valid case
        performTestCase(6, new User("newUser", "newPass", "user@example.com"), true); // Valid case

        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    private static void performTestCase(int testCase, User user, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);

        boolean result = validateUser(user);
        
        if (result != expectedPass) {
            System.out.println("***Failure*** User validation for username <" + user.getUsername() + "> failed." +
                    "\nExpected: " + (expectedPass ? "Valid" : "Invalid") +
                    ", but got: " + (result ? "Valid" : "Invalid") + "\n");
            numFailed++;
        } else {
            System.out.println("***Success*** User validation for username <" + user.getUsername() + "> passed.\n");
            numPassed++;
        }
    }

    private static boolean validateUser(User user) {
        // Check for null or empty values for username, password, and email
        if (user.getUsername() == null || user.getUsername().isEmpty() || 
            user.getPassword() == null || user.getPassword().isEmpty() || 
            user.getEmail() == null || user.getEmail().isEmpty()) {
            return false; // Validation fails if any required field is null or empty
        }
        return true; // Validation passes if all required fields are set
    }
}
