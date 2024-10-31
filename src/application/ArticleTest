package application;

import java.util.Arrays;
import java.util.List;

public class ArticleTest {
    static int numPassed = 0;
    static int numFailed = 0;

    public static void main(String[] args) {
        System.out.println("____________________________________________________________________________");
        System.out.println("\nArticle Testing Automation");

        // Test cases for Article class
        performTestCase(1, new Article(1L, "Beginner", "Sample Title", "Sample description", 
                                       Arrays.asList("keyword1", "keyword2"), "Sample body", 
                                       Arrays.asList("ref1", "ref2"), Arrays.asList("group1")), true); // Valid case
        performTestCase(2, new Article(2L, "Beginner", "", "Sample description", 
                                       Arrays.asList("keyword1"), "Sample body", 
                                       Arrays.asList("ref1"), Arrays.asList("group1")), false); // Invalid title
        performTestCase(3, new Article(3L, "Intermediate", "Sample Title", "", 
                                       Arrays.asList("keyword1"), "Sample body", 
                                       Arrays.asList("ref1"), Arrays.asList("group1")), false); // Invalid description
        performTestCase(4, new Article(4L, "Advanced", "Sample Title", "Sample description", 
                                       Arrays.asList("keyword1"), "", 
                                       Arrays.asList("ref1"), Arrays.asList("group1")), false); // Invalid body
        performTestCase(5, new Article(5L, "Expert", "Valid Title", "Valid description", 
                                       Arrays.asList("keyword1", "keyword2"), "Valid body", 
                                       Arrays.asList("ref1", "ref2"), Arrays.asList("group1")), true); // Valid case

        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    private static void performTestCase(int testCase, Article article, boolean expectedPass) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);

        boolean result = validateArticle(article);
        
        if (result != expectedPass) {
            System.out.println("***Failure*** Article validation for title <" + article.getTitle() + "> failed." +
                    "\nExpected: " + (expectedPass ? "Valid" : "Invalid") +
                    ", but got: " + (result ? "Valid" : "Invalid") + "\n");
            numFailed++;
        } else {
            System.out.println("***Success*** Article validation for title <" + article.getTitle() + "> passed.\n");
            numPassed++;
        }
    }

    private static boolean validateArticle(Article article) {
        // Check for null or empty values for title, body, and other essential fields
        if (article.getTitle() == null || article.getTitle().isEmpty() || 
            article.getDescription() == null || article.getDescription().isEmpty() || 
            article.getBody() == null || article.getBody().isEmpty()) {
            return false; // Validation fails if any required field is null or empty
        }
        return true; // Validation passes if all required fields are set
    }
}
