package com.higginss.dao;

import com.higginss.model.Article;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * <u>Design Notes</u>
 * <p>
 * Extend options = use MongoRepository if pagination and sorting capabilities are required else for basic CRUD use
 * CrudRespostory.
 * 
 * @author higginss
 */
public interface ArticleDao extends CrudRepository<Article, String> {

    @Override
    Article findOne(String id);
    
    List<Article> findByTopicsIn(List<String> topics);
}
