package com.mars_tech.shehriyar.top5.pojo;

import java.util.HashMap;

public class Comment {

    public String postID, id, userID, comment, userName;
    public long timestamp;

    public Comment(String postID, String id, String userID, String comment, long timestamp) {
        this.postID = postID;
        this.id = id;
        this.userID = userID;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public HashMap<String, Object> toJson() {
        HashMap<String, Object> json = new HashMap<>();

        json.put("userID", userID);
        json.put("comment", comment);
        json.put("timestamp", timestamp);

        return json;
    }
}
