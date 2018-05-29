package com.higginss.controller;

import com.higginss.dao.ArticleDao;
import com.higginss.model.Article;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <u>Article RESTful Web Service Layer</u>
 * <p>
 * <u>Design Notes</u>
 * <p>
 * For consideration: it might be beneficial to create individual nano-services for each CRUD type operation (but these
 * decisions depend on the use-cases and the minimal viable product suitable for the business use-case etc).
 * <p>
 * If the use-cases were to 'grow' and become more complex recommend introducing some form of delegation/mediator
 * pattern to handle/encapsulate the business logic and handle communications with the DAO (business object).
 *
 * @author higginss
 */
@RestController
@RequestMapping("/api/v1")
//@PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')") // alternative to using a security config bean.
public class ArticlesController {

    @Autowired
    private ArticleDao articleDao;

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
        articleDao.save(article);
        return article;
    }

    //TODO - add some update services...post v put is a design decision in these cases ?
    @PutMapping(value = "/article/")
    public Article updateArticle(@RequestBody Article article) {
        if (article.getId() != null && !article.getId().equals("")) {
            // For updating an article ensure it already has been persisted and update/replace all fields.
            articleDao.save(article);
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
        Article article = articleDao.findOne(id);
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
        return (List<Article>) articleDao.findAll();
    }

    @GetMapping(value = "/article/export")
    public List<Article> exportArticles() {
        List<Article> articles = (List<Article>) articleDao.findAll();
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
        return articleDao.findByTopicsIn(Arrays.asList(topics));
    }

    /**
     * Delete an article based on the unique article identifier (return code 404 if the article does not exist).
     *
     * @param id unique id.
     */
    @DeleteMapping(value = "/article/{id}")
    public void deleteArticle(@PathVariable String id) {
        // Could check to ensure the article exists first and ignore delete attempt if it does not.
        fetchArticle(id);
        // Only attempt to delete if it already exists.
        articleDao.delete(id);
    }
}
