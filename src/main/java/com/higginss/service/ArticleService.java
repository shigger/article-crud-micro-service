package com.higginss.service;

import com.higginss.controller.ArticleNotFoundException;
import com.higginss.dao.ArticleDao;
import com.higginss.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService { // Here KISS applies (keep it simple stupid so no interface)

    @Autowired
    private ArticleDao articleDao;

    public Article addArticle(Article article) {
        return articleDao.save(article);
    }

    public Article uploadArticle(Article article) {
        return articleDao.save(article);
    }

    public Article findArticleById(String id) {
       return articleDao.findOne(id);
    }

    public List<Article> findAllArticles() {
        return (List<Article>) articleDao.findAll();
    }

    public boolean deleteArticle(String id) {
        boolean success = false;
        // Could check to ensure the article exists first and ignore delete attempt if it does not.
        if (findArticleById(id) != null) {
            // Only attempt to delete if it already exists.
            articleDao.delete(id);
            success = true;
        }
        return success;
    }
}
