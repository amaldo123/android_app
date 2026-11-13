package com.app.androidnewsapp.callback;

import androidx.annotation.Keep;

import com.app.androidnewsapp.models.Tutorial;

import java.util.ArrayList;
import java.util.List;

@Keep
public class CallbackTutorial {

    public String status = "";
    public int count = -1;
    public int count_total = -1;
    public int pages = -1;
    public List<Tutorial> tutorials = new ArrayList<>();
    public List<Tutorial> posts = new ArrayList<>();

}

