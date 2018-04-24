package com.higginss.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <u>Design Notes</u>
 * <p>
 The topics are self-contained within each article document but perhaps could be stored/aggregated externally if
 deemed beneficial and if possible in MongoDB (similar to a 'join' in SQL).
 * 
 * @author higginss
 */
@Document(collection = "articles")
public class Article {

    @Id
    private String id;
    private String author;
    private String headline;
    private String content;
    private List<String> topics = new ArrayList<>();

    public Article() {
    }

    public Article(String author, String headline, String content, List<String>topics) {
        this.author = author;
        this.headline = headline;
        this.content = content;
        this.topics = topics;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    @Override
    public String toString() {
        return "Article{" + "id=" + id + ", author=" + author + ", headline=" + headline + ", content=" + content +
                ", topics=" + topics + '}';
    }   
}
