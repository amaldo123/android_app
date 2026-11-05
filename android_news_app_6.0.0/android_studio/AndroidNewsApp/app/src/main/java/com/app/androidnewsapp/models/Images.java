package com.app.androidnewsapp.models;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Images implements Serializable {

    public long nid = -1;
    public String image_name = "";
    public String content_type = "";
    public String video_id = "";
    public String video_url = "";

}
