package com.app.androidnewsapp.models;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Category implements Serializable {

    public long cid = -1;
    public String category_name = "";
    public String category_image = "";
    public long post_count = -1;

}
