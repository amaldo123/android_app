package com.app.androidnewsapp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.androidnewsapp.R;
import com.app.androidnewsapp.database.prefs.SharedPref;
import com.app.androidnewsapp.models.Tutorial;
import com.app.androidnewsapp.util.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class AdapterTutorial extends RecyclerView.Adapter<AdapterTutorial.ViewHolder> {

    private final Context context;
    private List<Tutorial> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private final SharedPref sharedPref;

    public interface OnItemClickListener {
        void onItemClick(View view, Tutorial obj, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AdapterTutorial(Context context, List<Tutorial> items) {
        this.context = context;
        this.items = items;
        this.sharedPref = new SharedPref(context);
    }

    public void setItems(List<Tutorial> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItems(List<Tutorial> newItems) {
        int startPos = this.items.size();
        this.items.addAll(newItems);
        notifyItemRangeInserted(startPos, newItems.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutorial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tutorial item = items.get(position);

        holder.title.setText(item.title);
        holder.category.setText(item.category);
        holder.duration.setText(item.duration);

        if (item.description != null && !item.description.isEmpty()) {
            holder.description.setText(Html.fromHtml(item.description, Html.FROM_HTML_MODE_LEGACY).toString().trim());
        } else {
            holder.description.setText("");
        }

        holder.views.setText(item.view_count + " views");

        boolean isVideo = "youtube".equalsIgnoreCase(item.content_type) || "url".equalsIgnoreCase(item.content_type) || (item.video_url != null && !item.video_url.isEmpty());
        holder.imgPlay.setVisibility(isVideo ? View.VISIBLE : View.GONE);

        String imageUrl = "";
        if ("youtube".equalsIgnoreCase(item.content_type) && item.video_url != null && !item.video_url.isEmpty()) {
            String videoId = extractYouTubeId(item.video_url);
            if (!videoId.isEmpty()) {
                imageUrl = Constant.YOUTUBE_IMG_FRONT + videoId + Constant.YOUTUBE_IMG_BACK;
            }
        }

        if (imageUrl.isEmpty() && item.image != null && !item.image.isEmpty()) {
            imageUrl = sharedPref.getBaseUrl() + "/upload/news/" + item.image;
        }

        if (!imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, item, position);
            }
        });
    }

    private String extractYouTubeId(String url) {
        if (url == null || url.trim().isEmpty()) return "";
        if (url.contains("v=")) {
            String[] parts = url.split("v=");
            if (parts.length > 1) {
                String id = parts[1];
                int amp = id.indexOf("&");
                return amp != -1 ? id.substring(0, amp) : id;
            }
        } else if (url.contains("youtu.be/")) {
            String[] parts = url.split("youtu.be/");
            if (parts.length > 1) {
                String id = parts[1];
                int q = id.indexOf("?");
                return q != -1 ? id.substring(0, q) : id;
            }
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public ImageView imgPlay;
        public TextView category;
        public TextView duration;
        public TextView title;
        public TextView description;
        public TextView views;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tutorial_image);
            imgPlay = itemView.findViewById(R.id.img_play);
            category = itemView.findViewById(R.id.tutorial_category);
            duration = itemView.findViewById(R.id.tutorial_duration);
            title = itemView.findViewById(R.id.tutorial_title);
            description = itemView.findViewById(R.id.tutorial_description);
            views = itemView.findViewById(R.id.tutorial_views);
        }
    }

}
