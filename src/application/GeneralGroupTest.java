package application;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class GeneralGroupTest {

    @Test
    void testAddAndRemoveArticles() {
        GeneralGroup group = new GeneralGroup("General Group");

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

        assertEquals(2, group.getArticles().size(), "The group should contain two articles.");
        assertTrue(group.getArticles().contains(article1), "Article 1 should be in the group.");
        assertTrue(group.getArticles().contains(article2), "Article 2 should be in the group.");

        group.removeArticle(article1);
        assertEquals(1, group.getArticles().size(), "The group should contain one article after removal.");
        assertFalse(group.getArticles().contains(article1), "Article 1 should no longer be in the group.");
    }

    @Test
    void testClearArticles() {
        GeneralGroup group = new GeneralGroup("General Group");

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
    void testAddUsers() {
        GeneralGroup group = new GeneralGroup("General Group");

        User admin = new User("admin", "admin@example.com", "admin");
        User instructor = new User("instructor", "instructor@example.com", "instructor");
        User student = new User("student", "student@example.com", "student");

        group.addAdmin(admin);
        group.addInstructors(instructor);
        group.addStudent(student);

        assertTrue(group.getAdmins().contains(admin), "Admin should be in the admin list.");
        assertTrue(group.getInstructors().contains(instructor), "Instructor should be in the instructor list.");
        assertTrue(group.getStudents().contains(student), "Student should be in the student list.");
    }

    @Test
    void testGetUsersByRole() {
        GeneralGroup group = new GeneralGroup("General Group");

        User admin = new User("admin", "admin@example.com", "admin");
        User instructor = new User("instructor", "instructor@example.com",  "instructor");
        User student = new User("student", "student@example.com", "student");

        group.addAdmin(admin);
        group.addInstructors(instructor);
        group.addStudent(student);

        List<User> admins = group.getUsersByRole("ADMIN");
        List<User> instructors = group.getUsersByRole("INSTRUCTOR");
        List<User> students = group.getUsersByRole("STUDENT");

        assertEquals(1, admins.size(), "There should be one admin in the group.");
        assertEquals(1, instructors.size(), "There should be one instructor in the group.");
        assertEquals(1, students.size(), "There should be one student in the group.");

        assertTrue(admins.contains(admin), "The admin list should contain the correct admin.");
        assertTrue(instructors.contains(instructor), "The instructor list should contain the correct instructor.");
        assertTrue(students.contains(student), "The student list should contain the correct student.");
    }

    @Test
    void testGetUsersByRoleInvalidRole() {
        GeneralGroup group = new GeneralGroup("General Group");

        List<User> result = group.getUsersByRole("INVALID_ROLE");
        assertTrue(result.isEmpty(), "The result should be empty for an invalid role.");
    }
}
