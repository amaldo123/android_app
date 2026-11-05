package com.app.androidnewsapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.androidnewsapp.R;
import com.app.androidnewsapp.config.AppConfig;
import com.app.androidnewsapp.database.prefs.SharedPref;
import com.app.androidnewsapp.models.Images;
import com.app.androidnewsapp.util.Constant;
import com.app.androidnewsapp.util.Tools;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterImageDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Images> images;
    private OnItemClickListener mOnItemClickListener;
    SharedPref sharedPref;

    public interface OnItemClickListener {
        void onItemClick(View view, Images obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterImageDetail(Context context, List<Images> images) {
        this.images = images;
        this.context = context;
        this.sharedPref = new SharedPref(context);
    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView postImage;

        public OriginalViewHolder(View v) {
            super(v);
            postImage = v.findViewById(R.id.image_detail);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View view;
        if (AppConfig.ENABLE_NEW_APP_DESIGN) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_image_detail, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_detail, parent, false);
        }
        vh = new OriginalViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder) {
            final Images post = images.get(position);
            final OriginalViewHolder vItem = (OriginalViewHolder) holder;

            if (post.content_type != null && post.content_type.equals("youtube")) {
                Glide.with(context)
                        .load(Constant.YOUTUBE_IMG_FRONT + post.video_id + Constant.YOUTUBE_IMG_BACK)
                        .thumbnail(Tools.requestBuilder(context))
                        .apply(Tools.glideRequestOptions())
                        .into(vItem.postImage);
            } else {
                String imageUrl = "";
                if (post.image_name != null) {
                    imageUrl = sharedPref.getBaseUrl() + "/upload/" + post.image_name.replace(" ", "%20");
                }
                Glide.with(context)
                        .load(imageUrl)
                        .thumbnail(Tools.requestBuilder(context))
                        .apply(Tools.glideRequestOptions())
                        .into(vItem.postImage);
            }

            vItem.postImage.setOnClickListener(v -> {
                int currentPos = vItem.getBindingAdapterPosition();
                if (mOnItemClickListener != null && currentPos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(v, post, currentPos);
                }
            });

        }
    }

    public void insertData(List<Images> items) {
        if (items == null) return;
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = items.size();
        this.images.addAll(items);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLoaded() {
        if (images == null) return;

        for (int i = images.size() - 1; i >= 0; i--) {
            if (images.get(i) == null) {
                images.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void resetListData() {
        this.images = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return images != null ? images.size() : 0;
    }

}