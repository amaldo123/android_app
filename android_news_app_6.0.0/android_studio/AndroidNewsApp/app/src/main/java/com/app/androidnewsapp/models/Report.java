package com.app.androidnewsapp.models;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Report implements Serializable {

    public String type;
    public String post_id;
    public String comment_id;
    public String user_id;
    public String reason;

}
