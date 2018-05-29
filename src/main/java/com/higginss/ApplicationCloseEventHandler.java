package com.higginss;

import com.higginss.dao.ArticleDao;
import com.higginss.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ApplicationCloseEventHandler implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    ArticleDao articleDao;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("closing XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        // In here export database to file but need correct event as db might already be closed !!!
        // There is no event before on exit the mongodb is closed so tricky to get to it.
        List<Article> articles = (List<Article>) articleDao.findAll();
        for (Article article : articles) System.out.println(article);
    }
}