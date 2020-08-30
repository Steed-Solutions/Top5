package com.mars_tech.shehriyar.top5.pojo;

import java.io.Serializable;

public class Post implements Serializable {

    public String id, type, name, link, text;
    public long likes, comments;
    public Category category;
    public boolean isLiked, isSaved;

    public Post() {
    }

    public Post(String id, String type, String name, String link, String text, long likes, long comments, Category category) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.link = link;
        this.text = text;
        this.likes = likes;
        this.comments = comments;
        this.category = category;
    }
}
