package com.higginss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higginss.model.Article;
import static org.hamcrest.Matchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.context.WebApplicationContext;

/**
 * Integration tests.
 *
 * @author higginss
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebApplicationContext.class})
@WebMvcTest(ArticlesController.class)
public class ArticlesControllerIT {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ArticlesController articlesController;

    public ArticlesControllerIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of uploadArticle method, of class ArticlesController.
     */
    @Test
    public void testUploadArticle() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Article article = new Article();
        article.setId("2");
        article.setHeadline("Article Headline");
        article.setAuthor("Article Author");
        article.setContent("Article Content");
        String jsonArticle = objectMapper.writeValueAsString(article);
        //when(articlesController.uploadArticle(Mockito.any(Article.class))).thenReturn(article);
        mockMvc.perform(post("/api/v1/article/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonArticle))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonArticle))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(article.getId())))
                .andExpect(jsonPath("$.author", is(article.getAuthor())))
                .andExpect(jsonPath("$.headline", is(article.getHeadline())))
                .andExpect(jsonPath("$.topics", is(article.getTopics())));
    }

    //TODO - add more tests to gain acceptable code coverage...
}
