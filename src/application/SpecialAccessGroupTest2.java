package application;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class SpecialAccessGroupTest2 {

    @Test
    void testAddAndRemoveArticles() {
        SpecialAccessGroup group = new SpecialAccessGroup("Test Group");
        
        Article article1 = new Article(
                1L,
                "Beginner",
                "Article 1",
                "This is the first article.",
                Arrays.asList("keyword1", "keyword2"),
                "Body of Article 1",
                Arrays.asList("ref1"),
                Arrays.asList("group1")
        );
        
        Article article2 = new Article(
                2L,
                "Advanced",
                "Article 2",
                "This is the second article.",
                Arrays.asList("keyword3", "keyword4"),
                "Body of Article 2",
                Arrays.asList("ref2"),
                Arrays.asList("group2")
        );

        group.addArticle(article1);
        group.addArticle(article2);

        assertEquals(2, group.getArticles().size(), "There should be two articles in the group.");
        assertTrue(group.getArticles().contains(article1), "Article 1 should be in the group.");
        assertTrue(group.getArticles().contains(article2), "Article 2 should be in the group.");
        
        group.removeArticle(article1);
        assertEquals(1, group.getArticles().size(), "There should be one article after removal.");
        assertFalse(group.getArticles().contains(article1), "Article 1 should no longer be in the group.");
    }
    
    @Test
    void testClearArticles() {
        SpecialAccessGroup group = new SpecialAccessGroup("Test Group");
        
        Article article = new Article(
                1L,
                "Beginner",
                "Article",
                "This is the article.",
                Arrays.asList("keyword"),
                "Body of Article",
                Arrays.asList("ref"),
                Arrays.asList("group")
        );

        group.addArticle(article);
        assertFalse(group.getArticles().isEmpty(), "Articles list should not be empty after adding an article.");

        group.clearArticles();
        assertTrue(group.getArticles().isEmpty(), "Articles list should be empty after clearing.");
    }

    @Test
    void testAddAndCheckUsers() {
        SpecialAccessGroup group = new SpecialAccessGroup("Test Group");

        User admin = new User("admin", "pass", "ADMIN");
        User instructorAd = new User("instructorAd", "pass", "INSTRUCTOR");
        User student = new User("student", "pass", "STUDENT");
        User instructorView = new User("instructorView", "pass", "INSTRUCTOR");

        group.addAdmin(admin);
        group.addInstructorsAd(instructorAd);
        group.addStudent(student);
        group.addInstructorsView(instructorView);

        assertTrue(group.getAdmins().contains(admin), "Admin should be in the admin list.");
        assertTrue(group.getInstructorsAd().contains(instructorAd), "Instructor Ad should be in the instructor ad list.");
        assertTrue(group.getStudents().contains(student), "Student should be in the student list.");
        assertTrue(group.getInstructorsView().contains(instructorView), "Instructor View should be in the instructor view list.");
    }

    @Test
    void testCanViewDecryptedContent() {
        SpecialAccessGroup group = new SpecialAccessGroup("Test Group");

        User instructorView = new User("instructorView", "pass", "INSTRUCTOR");
        User student = new User("student", "pass", "STUDENT");

        group.addInstructorsView(instructorView);
        group.addStudent(student);

        assertTrue(group.canInstructorViewBody(instructorView), "Instructor should have access to view decrypted content.");
        assertTrue(group.canStudentViewBody(student), "Student should have access to view decrypted content.");
    }

    @Test
    void testAccessWithoutPermissions() {
        SpecialAccessGroup group = new SpecialAccessGroup("Test Group");

        User instructorView = new User("instructor", "pass", "INSTRUCTOR");
        User student = new User("student", "pass", "STUDENT");

        assertFalse(group.canInstructorViewBody(instructorView), "Instructor without permissions should not have access.");
        assertFalse(group.canStudentViewBody(student), "Student without permissions should not have access.");
    }
}
