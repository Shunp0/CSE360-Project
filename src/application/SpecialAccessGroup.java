package application;

import java.util.ArrayList;
import java.util.List;
public class SpecialAccessGroup {
    private String groupName;
    private List<Article> articles; // Articles within this special access group
    private List<User> admins; // Admin users who can manage access to this group
    private List<User> instructorsAd; // Instructors who can manage access to this group
    private List<User> students; // Students who have access to view decrypted articles
    private List<User> instructorsView;  // Instructors who can manage access and view decrypted articles

    public SpecialAccessGroup(String groupName) {
        this.groupName = groupName;
        this.articles = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.instructorsAd = new ArrayList<>();
        this.students = new ArrayList<>();
        this.instructorsView = new ArrayList<>();
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

    public List<User> getInstructorsAd() {
        return instructorsAd;
    }

    public List<User> getInstructorsView() {
        return instructorsView;
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

    public void addInstructorsAd(User user) {
        if (!instructorsAd.contains(user)) {
            instructorsAd.add(user);
        }
    }

    public void addStudent(User user) {
        if (!students.contains(user)) {
            students.add(user);
        }
    }
    
    public void addInstructorsView(User user) {
        if (!instructorsView.contains(user)) {
            instructorsView.add(user);
        }
    }


    // Method to check if an instructor can view decrypted content
    public boolean canInstructorViewBody(User user) {
        return instructorsView.contains(user);
    }

    // Method to check if a student can view decrypted content
    public boolean canStudentViewBody(User user) {
        return students.contains(user);
    }
}
