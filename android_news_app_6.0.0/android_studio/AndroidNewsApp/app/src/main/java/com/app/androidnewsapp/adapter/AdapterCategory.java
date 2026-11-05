package com.app.androidnewsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.androidnewsapp.R;
import com.app.androidnewsapp.config.AppConfig;
import com.app.androidnewsapp.database.prefs.SharedPref;
import com.app.androidnewsapp.models.Category;
import com.app.androidnewsapp.util.Tools;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {

    Context context;
    private List<Category> items;
    private OnItemClickListener mOnItemClickListener;

    SharedPref sharedPref;

    public interface OnItemClickListener {
        void onItemClick(View view, Category obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterCategory(Context context, List<Category> items) {
        this.items = items;
        this.context = context;
        this.sharedPref = new SharedPref(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView categoryName;
        public TextView postCount;
        public LinearLayout lytParent;
        public ImageView categoryImage;

        public ViewHolder(View v) {
            super(v);
            categoryImage = v.findViewById(R.id.category_image);
            categoryName = v.findViewById(R.id.name);
            postCount = v.findViewById(R.id.post_count);
            lytParent = v.findViewById(R.id.lyt_parent);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Category c = items.get(position);

        String categoryName = c.category_name != null ? c.category_name : "";
        holder.categoryName.setText(HtmlCompat.fromHtml(categoryName, HtmlCompat.FROM_HTML_MODE_LEGACY));

        if (AppConfig.SHOW_POST_COUNT_IN_CATEGORY) {
            holder.postCount.setText(Tools.withSuffix(c.post_count) + " " + context.getResources().getString(R.string.txt_posts));
        } else {
            holder.postCount.setVisibility(View.GONE);
        }

        String imageUrl = "";
        if (c.category_image != null) {
            imageUrl = sharedPref.getBaseUrl() + "/upload/category/" + c.category_image.replace(" ", "%20");
        }

        Glide.with(context)
                .load(imageUrl)
                .thumbnail(Tools.requestBuilder(context))
                .apply(Tools.glideRequestOptions())
                .into(holder.categoryImage);

        holder.lytParent.setOnClickListener(view -> {
            int currentPos = holder.getBindingAdapterPosition();
            if (mOnItemClickListener != null && currentPos != RecyclerView.NO_POSITION) {
                mOnItemClickListener.onItemClick(view, c, currentPos);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListData(List<Category> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void resetListData() {
        this.items = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

}