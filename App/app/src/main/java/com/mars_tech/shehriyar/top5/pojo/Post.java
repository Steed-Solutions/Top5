package com.mars_tech.shehriyar.top5.pojo;

public class Post {

    public String id, type, name, content;
    public Category category;

    public Post() {
    }

    public Post(String id, String type, String name, String content, Category category) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.content = content;
        this.category = category;
    }
}
