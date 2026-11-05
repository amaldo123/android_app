package com.app.androidnewsapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.app.androidnewsapp.R;
import com.app.androidnewsapp.activity.ActivityPostDetails;
import com.app.androidnewsapp.adapter.AdapterPost;
import com.app.androidnewsapp.database.sqlite.DbHandler;
import com.app.androidnewsapp.models.Post;
import com.app.androidnewsapp.util.Constant;
import com.app.androidnewsapp.util.Tools;

import java.util.ArrayList;
import java.util.List;

public class FragmentFavorite extends Fragment {

    private View rootView;
    RecyclerView recyclerView;
    AdapterPost adapterPost;
    List<Post> posts = new ArrayList<>();
    DbHandler dbHandler;
    Tools tools;
    Activity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        if (activity == null && getActivity() != null) {
            activity = getActivity();
        }

        dbHandler = new DbHandler(activity != null ? activity : requireContext());
        tools = new Tools(activity != null ? activity : (Activity) requireContext());

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapterPost = new AdapterPost(activity != null ? activity : requireContext(), recyclerView, posts, false);
        recyclerView.setAdapter(adapterPost);

        adapterPost.setOnItemClickListener((v, obj, position) -> {
            if (activity == null) return;
            Intent intent = new Intent(activity, ActivityPostDetails.class);
            intent.putExtra(Constant.EXTRA_OBJC, obj);
            startActivity(intent);
        });

        adapterPost.setOnItemOverflowClickListener((view, obj, position) -> {
            if (activity != null) {
                View coordinator = activity.findViewById(R.id.coordinator_layout);
                if (coordinator != null) {
                    tools.showBottomSheetDialogMoreOptions(coordinator, obj, false, () -> {
                        if (isAdded()) {
                            displayData(dbHandler.getAllData());
                        }
                    });
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded()) {
            displayData(dbHandler.getAllData());
        }
    }

    public void displayData(List<Post> posts) {
        List<Post> favorites = new ArrayList<>();
        if (posts != null && !posts.isEmpty()) {
            favorites.addAll(posts);
        }
        adapterPost.resetListData();
        adapterPost.insertDataWithNativeAd(favorites);
        showNoItemView(favorites.isEmpty());
    }

    private void showNoItemView(boolean show) {
        if (rootView == null) return;

        View lyt_no_item = rootView.findViewById(R.id.lyt_no_item_later);
        ((TextView) rootView.findViewById(R.id.no_item_message)).setText(R.string.no_favorite_found);
        if (show) {
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            lyt_no_item.setVisibility(View.GONE);
        }
    }

}