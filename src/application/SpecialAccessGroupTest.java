package application;

import java.util.Arrays;

public class SpecialAccessGroupTest {
    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nSpecial Access Group Testing Automation");

        // Test cases for SpecialAccessGroup class
        performTestCase(1, createValidGroup(), true); // Valid group with all fields properly set
        performTestCase(2, createGroupWithNoArticles(), false); // Group with no articles
        performTestCase(3, createGroupWithEmptyName(), false); // Group with empty name
        performTestCase(4, createGroupWithValidViewingRights(), true); // Valid case with viewing rights

        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    private static void performTestCase(int testCase, SpecialAccessGroup group, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);

        boolean result = validateSpecialAccessGroup(group);

        if (result != expectedPass) {
            System.out.println("***Failure*** Validation for group <" + group.getGroupName() + "> failed." +
                    "\nExpected: " + (expectedPass ? "Valid" : "Invalid") +
                    ", but got: " + (result ? "Valid" : "Invalid") + "\n");
            numFailed++;
        } else {
            System.out.println("***Success*** Validation for group <" + group.getGroupName() + "> passed.\n");
            numPassed++;
        }
    }

    private static boolean validateSpecialAccessGroup(SpecialAccessGroup group) {
        // Check for null or empty group name
        if (group.getGroupName() == null || group.getGroupName().isEmpty()) {
            return false;
        }

        // Ensure the group has at least one article
        if (group.getArticles().isEmpty()) {
            return false;
        }

        // Ensure no duplicate admins
        if (group.getAdmins().size() != group.getAdmins().stream().distinct().count()) {
            return false;
        }

        // Ensure all articles are valid (dummy validation for simplicity)
        for (Article article : group.getArticles()) {
            if (article.getTitle() == null || article.getTitle().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    // Helper methods to create test groups
    private static SpecialAccessGroup createValidGroup() {
        SpecialAccessGroup group = new SpecialAccessGroup("Valid Group");
        group.addArticle(new Article(1L, "Beginner", "Title1", "Description1", Arrays.asList("kw1"), "Body1", Arrays.asList("ref1"), Arrays.asList("group1")));
        group.addAdmin(new User("admin1", "password", "Admin"));
        group.addInstructorsView(new User("instructor1", "password", "Instructor"));
        group.addStudent(new User("student1", "password", "Student"));
        return group;
    }


    private static SpecialAccessGroup createGroupWithNoArticles() {
        SpecialAccessGroup group = new SpecialAccessGroup("No Articles Group");
        group.addAdmin(new User("admin1", "password", "Admin"));
        return group; // No articles added
    }

    private static SpecialAccessGroup createGroupWithEmptyName() {
        SpecialAccessGroup group = new SpecialAccessGroup(""); // Empty group name
        group.addAdmin(new User("admin1", "password", "Admin"));
        group.addArticle(new Article(3L, "Advanced", "Title3", "Description3", Arrays.asList("kw3"), "Body3", Arrays.asList("ref3"), Arrays.asList("group3")));
        return group;
    }

    private static SpecialAccessGroup createGroupWithValidViewingRights() {
        SpecialAccessGroup group = new SpecialAccessGroup("Valid Viewing Rights Group");
        group.addArticle(new Article(4L, "Expert", "Title4", "Description4", Arrays.asList("kw4"), "Body4", Arrays.asList("ref4"), Arrays.asList("group4")));
        group.addAdmin(new User("admin1", "password", "Admin"));
        group.addInstructorsView(new User("instructor1", "password", "Instructor"));
        group.addStudent(new User("student1", "password", "Student"));
        return group;
    }
}
