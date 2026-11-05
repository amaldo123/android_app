package com.app.androidnewsapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat; // PERBAIKAN: Import HtmlCompat
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.app.androidnewsapp.R;
import com.app.androidnewsapp.config.AppConfig;
import com.app.androidnewsapp.database.prefs.AdsPref;
import com.app.androidnewsapp.database.prefs.SharedPref;
import com.app.androidnewsapp.models.Post;
import com.app.androidnewsapp.util.AdsManager;
import com.app.androidnewsapp.util.Constant;
import com.app.androidnewsapp.util.Tools;
import com.bumptech.glide.Glide;
import com.solodroidx.ads.nativead.NativeAdViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AdapterVideo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private final int VIEW_PROG = 0;
    private final int VIEW_ITEM = 1;
    private final int VIEW_AD = 2;
    private final int VIEW_NONE = 3;

    List<Post> posts;

    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private OnItemClickListener mOnItemClickListener;
    private OnItemOverflowClickListener mOnItemOverflowClickListener;
    boolean scrolling = false;

    SharedPref sharedPref;
    AdsPref adsPref;
    AdsManager adsManager;

    public interface OnItemClickListener {
        void onItemClick(View view, Post obj, int position);
    }

    public interface OnItemOverflowClickListener {
        void onItemOverflowClick(View view, Post obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public void setOnItemOverflowClickListener(final OnItemOverflowClickListener mItemOverflowClickListener) {
        this.mOnItemOverflowClickListener = mItemOverflowClickListener;
    }

    public AdapterVideo(Context context, RecyclerView view, List<Post> posts) {
        this.posts = posts;
        this.context = context;
        this.sharedPref = new SharedPref(context);
        this.adsPref = new AdsPref(context);
        this.adsManager = new AdsManager((Activity) context);
        lastItemViewDetector(view);
        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    scrolling = true;
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scrolling = false;
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView excerpt;
        public ImageView icDate;
        public TextView date;
        public ImageView icComment;
        public TextView comment;
        public ImageView image;
        public ImageView thumbnailVideo;
        public LinearLayout lytParent;
        public LinearLayout lytDate;
        public LinearLayout lytComment;
        public ImageView btnOverflow;

        public OriginalViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            excerpt = v.findViewById(R.id.excerpt);
            icDate = v.findViewById(R.id.ic_date);
            date = v.findViewById(R.id.date);
            icComment = v.findViewById(R.id.ic_comment);
            comment = v.findViewById(R.id.comment);
            image = v.findViewById(R.id.image);
            thumbnailVideo = v.findViewById(R.id.thumbnail_video);
            lytParent = v.findViewById(R.id.lyt_parent);
            lytDate = v.findViewById(R.id.lyt_date);
            lytComment = v.findViewById(R.id.lyt_comment);
            btnOverflow = v.findViewById(R.id.btn_overflow);
        }

    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
            vh = new OriginalViewHolder(v);
        } else if (viewType == VIEW_AD) {
            vh = adsManager.createNativeAdViewHolder(context, parent);
        } else if (viewType == VIEW_NONE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_none, parent, false);
            vh = new OriginalViewHolder(view);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OriginalViewHolder vItem) {
            final Post p = posts.get(position);

            String newsTitle = p.news_title != null ? p.news_title : "";
            String newsDesc = p.news_description != null ? p.news_description : "";

            vItem.title.setText(HtmlCompat.fromHtml(newsTitle, HtmlCompat.FROM_HTML_MODE_LEGACY));
            vItem.excerpt.setText(HtmlCompat.fromHtml(newsDesc, HtmlCompat.FROM_HTML_MODE_LEGACY));
            vItem.excerpt.setVisibility(View.GONE);

            if (AppConfig.SHOW_DATE_DISPLAY_AS_TIME_AGO) {
                vItem.date.setText(Tools.getTimeAgo(p.news_date));
            } else {
                vItem.date.setText(Tools.getFormatedDateSimple(p.news_date));
            }

            if (AppConfig.SHOW_POST_DATE) {
                vItem.lytDate.setVisibility(View.VISIBLE);
            } else {
                vItem.lytDate.setVisibility(View.GONE);
            }

            if (sharedPref.getLoginFeature().equals("yes")) {
                vItem.lytComment.setVisibility(View.VISIBLE);
            } else {
                vItem.lytComment.setVisibility(View.GONE);
            }

            vItem.comment.setText("" + Tools.withSuffix(p.comments_count));

            if (p.content_type != null && p.content_type.equals("Post")) {
                vItem.thumbnailVideo.setVisibility(View.GONE);
            } else {
                vItem.thumbnailVideo.setVisibility(View.VISIBLE);
            }

            if (p.content_type != null && p.content_type.equals("youtube")) {
                Glide.with(context)
                        .load(Constant.YOUTUBE_IMG_FRONT + p.video_id + Constant.YOUTUBE_IMG_BACK)
                        .thumbnail(Tools.requestBuilder(context))
                        .apply(Tools.glideRequestOptions())
                        .into(vItem.image);
            } else {
                String imageUrl = "";
                if (p.news_image != null) {
                    imageUrl = sharedPref.getBaseUrl() + "/upload/" + p.news_image.replace(" ", "%20");
                }
                Glide.with(context)
                        .load(imageUrl)
                        .thumbnail(Tools.requestBuilder(context))
                        .apply(Tools.glideRequestOptions())
                        .into(vItem.image);
            }

            if (sharedPref.getIsDarkTheme()) {
                vItem.btnOverflow.setColorFilter(ContextCompat.getColor(context, R.color.color_dark_icon), PorterDuff.Mode.SRC_IN);
                vItem.icDate.setColorFilter(ContextCompat.getColor(context, R.color.color_dark_icon), PorterDuff.Mode.SRC_IN);
                vItem.icComment.setColorFilter(ContextCompat.getColor(context, R.color.color_dark_icon), PorterDuff.Mode.SRC_IN);
                vItem.excerpt.setTextColor(ContextCompat.getColor(context, R.color.color_dark_text));
            } else {
                vItem.btnOverflow.setColorFilter(ContextCompat.getColor(context, R.color.color_light_icon), PorterDuff.Mode.SRC_IN);
                vItem.icDate.setColorFilter(ContextCompat.getColor(context, R.color.color_light_icon), PorterDuff.Mode.SRC_IN);
                vItem.icComment.setColorFilter(ContextCompat.getColor(context, R.color.color_light_icon), PorterDuff.Mode.SRC_IN);
                vItem.excerpt.setTextColor(ContextCompat.getColor(context, R.color.color_light_text));
            }

            vItem.lytParent.setOnClickListener(view -> {
                int currentPos = vItem.getBindingAdapterPosition();
                if (mOnItemClickListener != null && currentPos != RecyclerView.NO_POSITION) {
                    mOnItemClickListener.onItemClick(view, p, currentPos);
                }
            });

            vItem.btnOverflow.setOnClickListener(view -> {
                int currentPos = vItem.getBindingAdapterPosition();
                if (mOnItemOverflowClickListener != null && currentPos != RecyclerView.NO_POSITION) {
                    mOnItemOverflowClickListener.onItemOverflowClick(view, p, currentPos);
                }
            });

        } else if (holder instanceof NativeAdViewHolder) {
            adsManager.bindNativeAdViewHolder(context, (NativeAdViewHolder) holder);
        } else if (holder instanceof ProgressViewHolder) {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams spanParams = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
            if (getItemViewType(position) == VIEW_PROG || getItemViewType(position) == VIEW_AD) {
                spanParams.setFullSpan(true);
            } else {
                spanParams.setFullSpan(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (posts == null || position >= posts.size()) return VIEW_PROG;

        Post post = posts.get(position);
        if (post != null) {
            if (post.news_title == null || post.news_title.isEmpty()) {
                if (adsPref.getIsNativeAdPostList()) {
                    return VIEW_AD;
                } else {
                    return VIEW_NONE;
                }
            }
            return VIEW_ITEM;
        } else {
            return VIEW_PROG;
        }
    }

    public void insertData(List<Post> items) {
        if (items == null) return;
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = items.size();
        this.posts.addAll(items);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void insertDataWithNativeAd(List<Post> items) {
        if (items == null) return;
        setLoaded();
        int positionStart = getItemCount();
        if (items.size() >= adsPref.getNativeAdIndex()) {
            items.add(adsPref.getNativeAdIndex(), new Post());
        }
        int itemCount = items.size();
        this.posts.addAll(items);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLoaded() {
        loading = false;
        if (posts == null) return;

        for (int i = posts.size() - 1; i >= 0; i--) {
            if (posts.get(i) == null) {
                posts.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void setLoading() {
        if (getItemCount() != 0) {
            this.posts.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void resetListData() {
        this.posts = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void lastItemViewDetector(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager layoutManager) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastPos = getLastVisibleItem(layoutManager.findLastVisibleItemPositions(null));
                    if (!loading && lastPos == getItemCount() - 1 && onLoadMoreListener != null) {
                        int current_page = getItemCount() / (AppConfig.LOAD_MORE + 1);
                        onLoadMoreListener.onLoadMore(current_page);
                        loading = true;
                    }
                }
            });
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

    private int getLastVisibleItem(int[] into) {
        if (into == null || into.length == 0) return 0;
        int lastIdx = into[0];
        for (int i : into) {
            if (lastIdx < i) lastIdx = i;
        }
        return lastIdx;
    }

}