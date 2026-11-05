package com.app.androidnewsapp.callback;

import androidx.annotation.Keep;

import com.app.androidnewsapp.models.Images;
import com.app.androidnewsapp.models.Post;

import java.util.ArrayList;
import java.util.List;

@Keep
public class CallbackPostDetail {

    public String status = "";
    public Post post = null;
    public List<Images> images = new ArrayList<>();
    public List<Post> related = new ArrayList<>();

}
