package com.app.androidnewsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.androidnewsapp.R;
import com.app.androidnewsapp.database.prefs.SharedPref;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolder> {

    Context context;
    private static final String SEARCH_HISTORY_KEY = "_SEARCH_HISTORY_KEY";
    private static final int MAX_HISTORY_ITEMS = 25;

    private List<String> items;
    private OnItemClickListener onItemClickListener;
    private OnItemActionClickListener onItemActionClickListener;
    SharedPreferences sharedPreferences;
    SharedPref sharedPref;

    public interface OnItemClickListener {
        void onItemClick(View view, String viewModel, int pos);
    }

    public interface OnItemActionClickListener {
        void onItemActionClick(View view, String viewModel, int pos);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemActionClickListener(OnItemActionClickListener onItemActionClickListener) {
        this.onItemActionClickListener = onItemActionClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView imgHistory;
        public ImageButton imgArrowUp;
        public RelativeLayout lytParent;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            imgHistory = v.findViewById(R.id.ic_history);
            imgArrowUp = v.findViewById(R.id.ic_arrow_up);
            lytParent = v.findViewById(R.id.lyt_parent);
        }
    }

    public AdapterSearch(Context context) {
        this.sharedPreferences = context.getSharedPreferences("PREF_RECENT_SEARCH", Context.MODE_PRIVATE);
        this.sharedPref = new SharedPref(context);
        this.context = context;

        this.items = getSearchHistory();
        if (this.items != null) {
            Collections.reverse(this.items);
        } else {
            this.items = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vItem, int position) {
        final String p = items.get(position);

        vItem.title.setText(p);

        if (sharedPref.getIsDarkTheme()) {
            vItem.imgHistory.setColorFilter(ContextCompat.getColor(context, R.color.color_dark_icon), PorterDuff.Mode.SRC_IN);
            vItem.imgArrowUp.setColorFilter(ContextCompat.getColor(context, R.color.color_dark_icon), PorterDuff.Mode.SRC_IN);
        } else {
            vItem.imgHistory.setColorFilter(ContextCompat.getColor(context, R.color.color_light_icon), PorterDuff.Mode.SRC_IN);
            vItem.imgArrowUp.setColorFilter(ContextCompat.getColor(context, R.color.color_light_icon), PorterDuff.Mode.SRC_IN);
        }

        vItem.lytParent.setOnClickListener(v -> new Handler(Looper.getMainLooper()).postDelayed(()-> {
            int currentPos = vItem.getBindingAdapterPosition();
            if(onItemClickListener != null && currentPos != RecyclerView.NO_POSITION){
                onItemClickListener.onItemClick(v, p, currentPos);
            }
        }, 150));

        vItem.imgArrowUp.setOnClickListener(v -> new Handler(Looper.getMainLooper()).postDelayed(()-> {
            int currentPos = vItem.getBindingAdapterPosition();
            if(onItemActionClickListener != null && currentPos != RecyclerView.NO_POSITION){
                onItemActionClickListener.onItemActionClick(v, p, currentPos);
            }
        }, 150));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshItems() {
        this.items = getSearchHistory();
        if (this.items != null) {
            Collections.reverse(this.items);
        } else {
            this.items = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    private static class SearchObject implements Serializable {
        public List<String> items;

        public SearchObject(List<String> items) {
            this.items = items;
        }
    }

    @SuppressWarnings("RedundantCollectionOperation")
    public void addSearchHistory(String s) {
        SearchObject searchObject = new SearchObject(getSearchHistory());

        if (searchObject.items == null) {
            searchObject.items = new ArrayList<>();
        }

        if (searchObject.items.contains(s)) searchObject.items.remove(s);
        searchObject.items.add(s);
        if (searchObject.items.size() > MAX_HISTORY_ITEMS) searchObject.items.remove(0);
        String json = new Gson().toJson(searchObject, SearchObject.class);
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, json).apply();
    }

    private List<String> getSearchHistory() {
        String json = sharedPreferences.getString(SEARCH_HISTORY_KEY, "");
        if (json.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            SearchObject searchObject = new Gson().fromJson(json, SearchObject.class);
            if (searchObject != null && searchObject.items != null) {
                return searchObject.items;
            }
        } catch (Exception e) {
            Log.e("AdapterSearch", "Error parsing search history JSON", e);
        }

        return new ArrayList<>();
    }

    public void clearSearchHistory() {
        sharedPreferences.edit().clear().apply();
    }

}