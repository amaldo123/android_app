package com.app.androidnewsapp.models;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Tutorial implements Serializable {

    public int id = 0;
    public String title = "";
    public String category = "";
    public String description = "";
    public String image = "";
    public String video_url = "";
    public String content_type = "Post";
    public String duration = "5 min";
    public int status = 1;
    public long view_count = 0;
    public String created_at = "";
    public String last_update = "";

    public Tutorial() {
    }

    public Tutorial(int id, String title, String category, String description, String image, String video_url, String content_type, String duration) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.image = image;
        this.video_url = video_url;
        this.content_type = content_type;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

}

