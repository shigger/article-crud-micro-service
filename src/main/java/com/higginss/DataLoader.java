package com.higginss;

import com.higginss.dao.ArticleDao;
import com.higginss.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * This is run after the application has been successfully started to reload the embedded database from
 * previously exported data (if there exists such an export). On application closure we export all the
 * data in the database to persist it to disk.
 *
 * If you need to run some specific code once the SpringApplication has started, you can implement the
 * ApplicationRunner or CommandLineRunner interfaces. Both interfaces work in the same way and offer a
 * single run method, which is called just before SpringApplication.run(…​) completes.
 */
@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private ArticleDao articleDao;

    public void run(ApplicationArguments args) {
        articleDao.save(new Article("lala1", "lala", "lala", null));
        articleDao.save(new Article("lala2", "lala", "lala", null));
        articleDao.save(new Article("lala3", "lala", "lala", null));
    }
}

