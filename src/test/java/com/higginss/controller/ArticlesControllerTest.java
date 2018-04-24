package com.higginss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higginss.model.Article;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test for the RESTful MVC layer.
 *
 * @author higginss
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebApplicationContext.class})
@WebMvcTest(ArticlesController.class)
public class ArticlesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticlesController articlesController;

    public ArticlesControllerTest() {
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

    @Test
    public void testUploadArticle() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Article article = new Article();
        article.setId("2");
        article.setHeadline("Article Headline");
        article.setAuthor("Article Author");
        article.setContent("Article Content");
        String jsonArticle = objectMapper.writeValueAsString(article);
        when(articlesController.uploadArticle(Mockito.any(Article.class))).thenReturn(article);
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

    @Test
    public void testFetchArticleExists() throws Exception {
        Article article = new Article();
        article.setId("1");
        article.setHeadline("Article Headline");
        article.setAuthor("Article Author");
        article.setContent("Article Content");
        when(articlesController.fetchArticle("1")).thenReturn(article);
        mockMvc.perform(get("/api/v1/article/{id}", article.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(article.getId())))
                .andExpect(jsonPath("$.author", is(article.getAuthor())))
                .andExpect(jsonPath("$.headline", is(article.getHeadline())));

        verify(articlesController, times(1)).fetchArticle("1");
        verifyNoMoreInteractions(articlesController);
    }

    @Test
    public void testResourceDoesNotExist() throws Exception {
        when(articlesController.fetchArticle("1")).thenReturn(null);
        mockMvc.perform(get("/api/v1/article/{id}/resourcedoesnotexist", "1"))
                .andExpect(status().isNotFound());
        verify(articlesController, times(0)).fetchArticle("1");
        verifyNoMoreInteractions(articlesController);
    }

    @Test
    public void testFetchArticleNotFound() throws Exception {
        when(articlesController.fetchArticle("-1")).thenThrow(new ArticleNotFoundException("article not to be found"));
        mockMvc.perform(get("/api/v1/article/{id}", "-1"))
                .andExpect(status().isNotFound());
    }
    
    // TODO: add more tests to complete code coverage...adding in extreme/error cases...
}
