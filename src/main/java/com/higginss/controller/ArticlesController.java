package com.higginss.controller;

import com.higginss.model.Article;
import com.higginss.service.ArticleService;
import com.higginss.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <u>Article RESTful Web Service Layer</u>
 * <p>
 * For consideration: it might be beneficial to create individual nano-services for each CRUD type operation (but these
 * decisions depend on the use-cases and the minimal viable product suitable for the business use-case etc).
 * </p>
 * <p>
 * If the use-cases were to 'grow' and become more complex recommend introducing some form of delegation/mediator
 * pattern to handle/encapsulate the business logic and handle communications with the DAO (business object).
 * </p>
 * <p>
 * Any Spring @RestController in a Spring Boot application will render JSON response by default as long as Jackson2
 * [jackson-databind] is on the classpath. In a web app [spring-boot-starter-web], it transitively gets included, no
 * need to explicitly include it.
 * </p>
 * <p>
 * Consider returning all responses as ResponseEntity - decide if better or overkill ?
 * </p>
 * The object data will be written directly to the HTTP response as JSON.
 *
 * @author higginss
 */
@RestController
@RequestMapping("/api/v1")
//@PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')") // alternative to using a security config bean.
public class ArticlesController {

    private final ArticleService articleService;

    @Autowired
    public ArticlesController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Upload an article to the database: example uri = http://localhost:8080/api/v1/article/ with posted data =
     * {"author":"Stephen Higgins", "headline":"MongoDB Rocks!", "content":"This is an experiment in lightweight
     * micro-service work.", "topics":["technology","news"]} thus creating a new article all the time.
     *
     * @param article the article to upload containing content and a list of topics (treated as embedded documents).
     * @return the article just uploaded containing the unique identifier assigned to it.
     */
    @PostMapping(value = "/article")
    public Article uploadArticle(@RequestBody Article article) {
        articleService.uploadArticle(article);
        return article;
        //Alternative: return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //TODO - add some update services...post v put is a design decision in these cases ?
    @PutMapping(value = "/article/")
    public Article updateArticle(@RequestBody Article article) {
        if (article.getId() != null && !article.getId().equals("")) {
            // For updating an article ensure it already has been persisted and update/replace all fields.
            articleService.uploadArticle(article);
        } else {
            // Is this an invalid invocation as we expect to update the article not create a new one !
            throw new ArticleNotFoundException("id-" + "");
        }
        return article;
    }

    /**
     * Fetch an article back based on the unique article identifier: example uri =
     * http://localhost:8080/api/v1/article/5ade207a15b2d72580dd825d (return code 404 if the article does not exist)
     *
     * @param id unique identifier (as passed back on a successful create/upload of an article).
     * @return the fetched article.
     */
    @GetMapping(value = "/article/{id}")
    public Article fetchArticle(@PathVariable String id) {
        Article article = articleService.findArticleById(id);
        if (article == null) {
            // Handle not found article with the correct handling.
            throw new ArticleNotFoundException("id-" + id);
        }
        return article;
    }

    @GetMapping(value = "/admin/{something}")
    public void doSomeAdminStuff(@PathVariable String something) {
        System.out.println("Doing admin stuff");
    }

    /**
     * Fetch all articles back (no pagination/sorting implemented): example uri =
     * http://localhost:8080/api/v1/article/all
     * <p>
     * If no articles are found return an empty list for processing.
     *
     * @return the fetched articles.
     */
    @GetMapping(value = "/article/all")
    public List<Article> fetchArticles() {
        return articleService.findAllArticles();
    }

    @GetMapping(value = "/article/export")
    public List<Article> exportArticles() {
        List<Article> articles = articleService.findAllArticles();
        // Now we have all the article we can write it to a persistent file store for safe-keeping.
        // Then on startup we could load this file into the database by calling the post for every
        // article (bulk load). There are export/import mongodb command line options that could be
        // called perhaps as an alternative solution.
        // Actually this should not be an api as not user facing operation but should be invoked
        // automatically on spring boot startup and termination to import and export.
        return articles;
    }

    /**
     * Fetch all articles across the topics supplied. Example URI:
     * http://localhost:8080/api/v1/article?topics[]=sport,technology - bring back all sport and technology articles.
     * <p>
     * If no articles are found return an empty list for processing.
     * <p>
     * <u>Design Notes</u>
     * <p>
     * URI design option is category=1&category=2 etc or construct a generic model for 'full' query handling.
     * <p>
     *
     * @param topics array of topics to return articles against.
     * @return list of articles across the topics supplied.
     */
    @GetMapping(value = "/article")
    public List<Article> fetchArticlesByTopic(@RequestParam(value = "topics[]") String[] topics) {
        return null; //articleDao.findByTopicsIn(Arrays.asList(topics));
    }

    /**
     * Delete an article based on the unique article identifier (return code 404 if the article does not exist).
     *
     * @param id unique id.
     */
    @DeleteMapping(value = "/article/{id}")
    public void deleteArticle(@PathVariable String id) {
        if (!articleService.deleteArticle(id)) {
            // A failing <=> article not found typically here - other unexpected errors would be propagated as normal.
            throw new ArticleNotFoundException("Article not found to delete for id = " + id);
        }
    }

    //
    // IMAGE SERVICES FOR AN ARTICLE
    //

    /**
     * Add an image to an existing article (add not replace): uses multipart/form-data instead of JSON (fully RESTful)
     * In this case each image is 1-1 with an article and so is modelled as part of the article (identified by article)
     * and it is expected that the article is created first (in this use-case).
     * The file is pushed over HTTP POST with encoding type “multipart/form-data” from the client to our web-service.
     * This way you can add multiple parameters to the POST request in addition to the file.
     */
    @PostMapping(value = "/article/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@PathVariable String id,
                                         @RequestParam("file") MultipartFile file) { //ResponseEntity represents entire HTTP response (might be overkill!)
        System.out.println("Single file image (" + file.getOriginalFilename() + ") upload request for article with id = " + id);
        if (file == null || file.isEmpty()) {
            return new ResponseEntity("Please select an image file!", HttpStatus.OK);
        }
        // Now store the image in the database for the article adding a new image keeping existing ones (in this case).
        return new ResponseEntity("Successfully uploaded image file - " +
                file.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * This version differs in the URI and the fact it returns default status codes (implicitly handled) but no body.
     * So on success returns code 200 by default and errors as handled.
     */
    @PostMapping(value = "/article/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadImageAlternative(@RequestParam String id,
                                       @RequestParam("file") MultipartFile file) {
        System.out.println("Single file image (" + file.getOriginalFilename() + ") upload request for article with id = " + id);
    }
}
