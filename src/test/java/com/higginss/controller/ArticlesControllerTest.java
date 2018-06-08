package com.higginss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higginss.model.Article;
import com.higginss.security.SecurityConfig;
import com.higginss.service.ArticleService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test for the RESTful Article API (with mocked service object) testing security, access and interface.
 * <p>
 * Unit testing requires that the source code is composed in such a way that dependencies between modules can be easily
 * neutralized with mocks. In addition, unit testing requires that functions are well isolated from each other.
 * <p>
 * When we are unit testing a rest service, we would want to launch only the specific controller and the related MVC
 * Components. WebMvcTest annotation is used for unit testing Spring MVC application. This can be used when a test
 * focuses only Spring MVC components. Using this annotation will disable full auto-configuration and only apply
 * configuration relevant to MVC tests.
 *
 * @author higginss
 */
//SpringRunner is short hand for SpringJUnit4ClassRunner providing functionality to launch Spring TestContext Framework.
@RunWith(SpringRunner.class)
// Following is important: need to add the security config context here or the roles are not used (but defaults).
// @ContextConfiguration defines class-level metadata that is used to determine how to load and configure an
// ApplicationContext for integration tests.
@ContextConfiguration(classes = {SecurityConfig.class, ArticlesController.class})
// By default, tests annotated with @WebMvcTest will also auto-configure Spring Security and MockMvc. Using this
// annotation will disable full auto-configuration and instead apply only configuration relevant to MVC tests.
@WebMvcTest(value = ArticlesController.class, secure = true)
// Think you can also use whole context by @SpringBootContext which might simplify above.
// @RunWith(SpringRunner.class)
 //@SpringBootTest
public class ArticlesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

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
    public void contextLoads() throws Exception {
        //assertThat(articlesController).isNotNull();
    }

    @Test
    @WithMockUser(username = "user1", password = "secret1", roles = "USER")
    public void testUploadArticle() throws Exception {
        // Given...some context...
        ObjectMapper objectMapper = new ObjectMapper();
        Article article = new Article();
        article.setId("2");
        article.setHeadline("Article Headline");
        article.setAuthor("Article Author");
        article.setContent("Article Content");
        String jsonArticle = objectMapper.writeValueAsString(article);
        // When...some action is carried out...
        when(articleService.uploadArticle(Mockito.any(Article.class))).thenReturn(article);
        // Then...a particular set of observable consequences should obtain...
        this.mockMvc.perform(post("/api/v1/article/")
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
    @WithMockUser(username = "user1", password = "secret1", roles = "USER")
    public void testFetchArticleExists() throws Exception {
        Article article = new Article();
        article.setId("1");
        article.setHeadline("Article Headline");
        article.setAuthor("Article Author");
        article.setContent("Article Content");
        when(articleService.findArticleById("1")).thenReturn(article);
        this.mockMvc.perform(get("/api/v1/article/{id}", article.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(article.getId())))
                .andExpect(jsonPath("$.author", is(article.getAuthor())))
                .andExpect(jsonPath("$.headline", is(article.getHeadline())));

        //verify(articlesController, times(1)).fetchArticle("1");
        //verifyNoMoreInteractions(articlesController);
    }

    @Test
    @WithMockUser(username = "user1", password = "secret1", roles = "USER")
    public void testResourceDoesNotExist() throws Exception {
        when(articleService.findArticleById("1")).thenReturn(null);
        this.mockMvc.perform(get("/api/v1/article/{id}/resourcedoesnotexist", "1"))
                .andExpect(status().isNotFound());
        //verify(articlesController, times(0)).fetchArticle("1");
        //verifyNoMoreInteractions(articlesController);
    }

    @Test
    @WithMockUser(username = "user1", password = "secret1", roles = "USER")
    public void testFetchArticleNotFound() throws Exception {
        when(articleService.findArticleById("-1")).thenThrow(new ArticleNotFoundException("article not to be found"));
        this.mockMvc.perform(get("/api/v1/article/{id}", "-1"))
                .andExpect(status().isNotFound());
    }

    /**
     * Test security with lack of authentication on user protected URI resources.
     *
     * @throws Exception
     */
    @Test
    public void testNotAuthorisedAtUserLevel() throws Exception {
        // No basic auth details passed over so should be unauthorised.
        this.mockMvc.perform(get("/api/v1/article/12345")).andExpect(status().isUnauthorized());
    }

    /**
     * Test security with lack of authentication on user protected URI resources.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "user1", password = "secret1", roles = "USER")
    public void testAuthorisedAtUserLevel() throws Exception {
        // Auth ok so should continue ok throwing not found exception and that status back (not unauthorised).
        this.mockMvc.perform(get("/api/v1/article/12345")).andExpect(status().isNotFound());
    }

    /**
     * Test security with lack of authentication on an admin resource using a user resource role.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "user1", password = "secret1", roles = "USER")
    public void testForbiddenAtUserLevel() throws Exception {
        // No basic auth details passed over so should be unauthorised.
        this.mockMvc.perform(get("/api/v1/admin/12345")).andExpect(status().isForbidden());
    }

    /**
     * Test security with lack of authentication on an admin resource using a user resource role.
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(username = "user1", password = "secret1", roles = "ADMIN")
    public void testAuthorisedAtAdminLevel() throws Exception {
        // No basic auth details passed over so should be unauthorised.
        this.mockMvc.perform(get("/api/v1/admin/12345")).andExpect(status().isOk());
    }

    // TODO: add more tests to complete code coverage...adding in extreme/error cases...add before coding to TDD.
}
