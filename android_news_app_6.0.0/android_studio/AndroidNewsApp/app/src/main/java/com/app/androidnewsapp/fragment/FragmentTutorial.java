package com.app.androidnewsapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.androidnewsapp.R;
import com.app.androidnewsapp.adapter.AdapterTutorial;
import com.app.androidnewsapp.callback.CallbackTutorial;
import com.app.androidnewsapp.config.AppConfig;
import com.app.androidnewsapp.database.prefs.SharedPref;
import com.app.androidnewsapp.models.Tutorial;
import com.app.androidnewsapp.rest.RestAdapter;
import com.app.androidnewsapp.util.Tools;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTutorial extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private AdapterTutorial adapterTutorial;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerViewContainer;
    private View lytNoItem;
    private View lytFailed;
    private SharedPref sharedPref;
    private final List<Tutorial> tutorialList = new ArrayList<>();
    private Call<CallbackTutorial> callbackCall = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        Context context = requireContext();
        sharedPref = new SharedPref(context);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);
        shimmerViewContainer = rootView.findViewById(R.id.shimmer_view_container);
        lytNoItem = rootView.findViewById(R.id.lyt_no_item);
        lytFailed = rootView.findViewById(R.id.lyt_failed);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);

        adapterTutorial = new AdapterTutorial(context, tutorialList);
        recyclerView.setAdapter(adapterTutorial);

        adapterTutorial.setOnItemClickListener((view, obj, position) -> {
            if ("youtube".equalsIgnoreCase(obj.content_type) || (obj.video_url != null && obj.video_url.contains("youtube"))) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(obj.video_url)));
            } else if (obj.video_url != null && !obj.video_url.isEmpty()) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(obj.video_url)));
            } else {
                // Show tutorial in browser or dialog
                if (obj.description != null && !obj.description.isEmpty()) {
                    new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireActivity())
                            .setTitle(obj.title)
                            .setMessage(android.text.Html.fromHtml(obj.description, android.text.Html.FROM_HTML_MODE_LEGACY))
                            .setPositiveButton("Close", null)
                            .show();
                }
            }
        });

        swipeRefreshLayout.setColorSchemeColors(requireContext().getColor(R.color.color_primary));
        swipeRefreshLayout.setOnRefreshListener(this::loadTutorials);

        Button btnRetry = rootView.findViewById(R.id.failed_retry);
        if (btnRetry != null) {
            btnRetry.setOnClickListener(v -> loadTutorials());
        }

        loadTutorials();

        return rootView;
    }

    private void loadTutorials() {
        if (callbackCall != null && callbackCall.isExecuted()) {
            callbackCall.cancel();
        }

        shimmerViewContainer.setVisibility(View.VISIBLE);
        shimmerViewContainer.startShimmer();
        lytNoItem.setVisibility(View.GONE);
        lytFailed.setVisibility(View.GONE);

        String baseUrl = sharedPref.getBaseUrl();
        if (baseUrl == null || baseUrl.isEmpty()) {
            baseUrl = AppConfig.SERVER_KEY;
        }

        callbackCall = RestAdapter.createAPI(baseUrl).getTutorials(AppConfig.REST_API_KEY, 1, 30);
        callbackCall.enqueue(new Callback<CallbackTutorial>() {
            @Override
            public void onResponse(@NonNull Call<CallbackTutorial> call, @NonNull Response<CallbackTutorial> response) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);

                CallbackTutorial resp = response.body();
                if (resp != null && "ok".equalsIgnoreCase(resp.status)) {
                    List<Tutorial> items = resp.tutorials != null && !resp.tutorials.isEmpty() ? resp.tutorials : resp.posts;
                    if (items != null && !items.isEmpty()) {
                        tutorialList.clear();
                        tutorialList.addAll(items);
                        adapterTutorial.setItems(tutorialList);
                        recyclerView.setVisibility(View.VISIBLE);
                        lytNoItem.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        lytNoItem.setVisibility(View.VISIBLE);
                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    lytFailed.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CallbackTutorial> call, @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    swipeRefreshLayout.setRefreshing(false);
                    shimmerViewContainer.stopShimmer();
                    shimmerViewContainer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    lytFailed.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (callbackCall != null) {
            callbackCall.cancel();
        }
    }
}
