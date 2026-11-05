package com.app.androidnewsapp.callback;

import androidx.annotation.Keep;

import com.app.androidnewsapp.models.Comments;

import java.util.ArrayList;
import java.util.List;

@Keep
public class CallbackComments {

    public String status = "";
    public int count = -1;
    public List<Comments> comments = new ArrayList<>();

}
