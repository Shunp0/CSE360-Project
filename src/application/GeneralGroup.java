package application;

import java.util.ArrayList;
import java.util.List;
public class GeneralGroup {
    private String groupName;
    private List<Article> articles; // Articles within this special access group
    private List<User> admins; // Admin users who can manage access to this group
    private List<User> instructors; // Instructors who can manage access to this group
    private List<User> students; // Students who have access to view decrypted articles

    public GeneralGroup(String groupName) {
        this.groupName = groupName;
        this.articles = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.instructors = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    // Getters and setters
    public String getGroupName() {
        return groupName;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public List<User> getAdmins() {
        return admins;
    }

    public List<User> getInstructors() {
        return instructors;
    }
    
    public List<User> getStudents() {
        return students;
    }

    // Methods to add/remove users and articles
    public void addArticle(Article article) {
        articles.add(article);
    }
    
    public void removeArticle(Article article) {
    	articles.remove(article);
    }
    
    public void clearArticles()
    {
    	articles.clear();
    }

    public void addAdmin(User user) {
        if (!admins.contains(user)) {
            admins.add(user);
        }
    }

    public void addInstructors(User user) {
        if (!instructors.contains(user)) {
            instructors.add(user);
        }
    }

    public void addStudent(User user) {
        if (!students.contains(user)) {
            students.add(user);
        }
    }
    
 // Method to get users by role from the GeneralGroup
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        
        // Check the role and add the corresponding list of users
        switch (role) {
            case "ADMIN":
                users.addAll(admins);
                break;
            case "INSTRUCTOR":
                users.addAll(instructors);
                break;
            case "STUDENT":
                users.addAll(students);
                break;
            default:
                System.out.println("Invalid role: " + role);
                break;
        }
        
        return users;
    }

    
}
