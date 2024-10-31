package application;

import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Article Class </p>
 * 
 * <p> Description: This class models a help article in the system, containing relevant attributes 
 * such as title, description, body, keywords, and grouping identifiers. </p>
 *
 * @version 1.00 10-30-2024 Phase 2
 */
public class Article {
    private long id; // Unique identifier for the article
    private String level; // Level of the article (e.g., beginner, intermediate)
    private String title; // Title of the article
    private String description; // Short description or abstract
    private List<String> keywords; // Keywords for search
    private String body; // Body content of the article
    private List<String> references; // Links to reference materials
    private List<String> groups; // Groups the article belongs to

    // Constructor
    public Article(long id, String level, String title, String description, List<String> keywords, String body, List<String> references, List<String> groups) {
        this.id = id;
        this.level = level;
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.body = body;
        this.references = references;
        this.groups = groups;
    }

    // Getters and Setters
    public void setId(long ID) {
    	this.id = ID;
    }
    public long getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getReferences() {
        return references;
    }

    public void setReferences(List<String> references) {
        this.references = references;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public static List<String> listArticles(List<Article> articles) {
        List<String> articleList = new ArrayList<>();
        for (Article article : articles) {
            StringBuilder sb = new StringBuilder();
            sb.append("Title: ").append(article.getTitle()).append("\n");
            sb.append("Description: ").append(article.getDescription()).append("\n");
            sb.append("Keywords: ").append(String.join(", ", article.getKeywords())).append("\n");
            sb.append("Groups: ").append(String.join(", ", article.getGroups())).append("\n");
            sb.append("References: ").append(String.join(", ", article.getReferences())).append("\n");
            sb.append("-----------------------------\n");
            articleList.add(sb.toString());
        }
        return articleList;
	}
}
