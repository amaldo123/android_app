package com.app.androidnewsapp.callback;

import androidx.annotation.Keep;

import com.app.androidnewsapp.models.Ads;
import com.app.androidnewsapp.models.App;
import com.app.androidnewsapp.models.License;
import com.app.androidnewsapp.models.Placement;
import com.app.androidnewsapp.models.Settings;

@Keep
public class CallbackSettings {

    public String status = "";
    public App app = null;
    public Settings settings = null;
    public Ads ads = null;

    public Placement ads_placement = null;
    public License license = null;

}