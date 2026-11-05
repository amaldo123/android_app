package com.app.androidnewsapp.models;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Comments implements Serializable {

    public String comment_id = "";
    public String user_id = "";
    public String name = "";
    public String image = "";
    public String date_time = "";
    public String content = "";

}
