package com.higginss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higginss.model.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) // Provides a bridge between Spring Boot test features and JUnit.
//@WebMvcTest(ArticlesController.class)
public class ArticlesControllerITest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ArticleService articleServiceMock;

    @Test
    public void contextLoads() throws Exception {
        //assertThat(articlesController).isNotNull();
    }

    @Test
    public void testaddArticle() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Article article = new Article();
        article.setId("2");
        article.setHeadline("Article Headline");
        article.setAuthor("Article Author");
        article.setContent("Article Content");
        String jsonArticle = objectMapper.writeValueAsString(article);
//        when(articleServiceMock.addArticle(Mockito.any(Article.class))).thenReturn(article);
//        mockMvc.perform(post("/api/v1/article/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonArticle))
//                .andExpect(status().isOk())
//                .andExpect(content().string(jsonArticle))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.id", is(article.getId())))
//                .andExpect(jsonPath("$.author", is(article.getAuthor())))
//                .andExpect(jsonPath("$.headline", is(article.getHeadline())))
//                .andExpect(jsonPath("$.topics", is(article.getTopics())));
    }
}
