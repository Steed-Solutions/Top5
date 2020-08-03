package com.mars_tech.shehriyar.top5.pojo;

public class Post {

    public String id, type, name, link, text;
    public Category category;

    public Post() {
    }

    public Post(String id, String type, String name, String link, String text, Category category) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.link = link;
        this.text = text;
        this.category = category;
    }
}
