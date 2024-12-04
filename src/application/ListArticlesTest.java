package application;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class ListArticlesTest {

    @Test
    void testListArticlesTest() {
        Article article1 = new Article(
                1L,
                "Beginner",
                "Article 1",
                "This is the first article.",
                Arrays.asList("keyword1", "keyword2"),
                "Body of Article 1",
                Arrays.asList("ref1", "ref2"),
                Arrays.asList("group1", "group2")
        );

        Article article2 = new Article(
                2L,
                "Advanced",
                "Article 2",
                "This is the second article.",
                Arrays.asList("keyword3", "keyword4"),
                "Body of Article 2",
                Arrays.asList("ref3"),
                Arrays.asList("group2")
        );

        List<Article> articles = Arrays.asList(article1, article2);
        List<String> result = Article.listArticles(articles);

        assertEquals(2, result.size(), "The list should contain two formatted articles.");

        String expectedArticle1 = """
                Title: Article 1
                Description: This is the first article.
                Keywords: keyword1, keyword2
                Groups: group1, group2
                References: ref1, ref2
                -----------------------------
                """;
        String expectedArticle2 = """
                Title: Article 2
                Description: This is the second article.
                Keywords: keyword3, keyword4
                Groups: group2
                References: ref3
                -----------------------------
                """;

        assertEquals(expectedArticle1, result.get(0), "The first article's formatted string is incorrect.");
        assertEquals(expectedArticle2, result.get(1), "The second article's formatted string is incorrect.");
    }

    @Test
    void testListArticlesEmptyList() {
        List<String> result = Article.listArticles(Collections.emptyList());
        assertTrue(result.isEmpty(), "The result should be empty when no articles are provided.");
    }
}
