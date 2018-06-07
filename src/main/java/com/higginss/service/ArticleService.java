package com.higginss.service;

import com.higginss.dao.ArticleDao;
import com.higginss.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleService { // Here KISS applies (keep it simple stupid so no interface)

    public final ArticleDao articleDao;

    @Autowired
    public ArticleService(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public Article addArticle(Article article) {
        return  articleDao.save(article);
    }
}
