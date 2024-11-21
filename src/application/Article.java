package application;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;
import java.nio.charset.StandardCharsets;
public class Article {
    private long id; // Unique identifier for the article
    private String level; // Level of the article (e.g., beginner, intermediate)
    private String title; // Title of the article
    private String description; // Short description or abstract
    private List<String> keywords; // Keywords for search
    private String body; // Body content of the article
    private List<String> references; // Links to reference materials
    private List<String> groups; // Groups the article belongs to
    private boolean encrypted;
    private static final String encryptionKey = "1234567890123456";

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
        this.encrypted = false;
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


    public void encryptBody() {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(), "AES");

            // Set up the AES cipher in encryption mode
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(body.getBytes(StandardCharsets.UTF_8));

            // Store the encrypted body as a Base64 string
            this.body = Base64.getEncoder().encodeToString(encryptedBytes);
            this.encrypted = true;  // Mark as encrypted
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBody() {
        if (encrypted) {
            return decryptBody();
        }
        return body;
    }
    
    public void setBody(String body)
    {
    	this.body = body;
    }

    public String decryptBody() {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decodedBytes = Base64.getDecoder().decode(body);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error decrypting content.";
        }
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
