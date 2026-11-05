package com.app.androidnewsapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.androidnewsapp.R;
import com.app.androidnewsapp.activity.ActivityCategoryDetails;
import com.app.androidnewsapp.activity.MainActivity;
import com.app.androidnewsapp.adapter.AdapterCategory;
import com.app.androidnewsapp.callback.CallbackCategories;
import com.app.androidnewsapp.config.AppConfig;
import com.app.androidnewsapp.database.prefs.SharedPref;
import com.app.androidnewsapp.models.Category;
import com.app.androidnewsapp.rest.ApiInterface;
import com.app.androidnewsapp.rest.RestAdapter;
import com.app.androidnewsapp.util.Constant;
import com.app.androidnewsapp.util.Tag;
import com.app.androidnewsapp.util.Tools;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCategory extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AdapterCategory adapterCategory;
    private Call<CallbackCategories> callbackCall = null;
    private ShimmerFrameLayout lytShimmer;
    SharedPref sharedPref;
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
        rootView = inflater.inflate(R.layout.fragment_category, container, false);

        if (activity == null && getActivity() != null) {
            activity = getActivity();
        }

        sharedPref = new SharedPref(activity != null ? activity : requireContext());

        lytShimmer = rootView.findViewById(R.id.shimmer_view_container);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout_category);
        swipeRefreshLayout.setColorSchemeResources(R.color.color_light_primary);
        recyclerView = rootView.findViewById(R.id.recyclerViewCategory);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        adapterCategory = new AdapterCategory(activity != null ? activity : requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adapterCategory);

        adapterCategory.setOnItemClickListener((v, obj, position) -> {
            if (activity == null) return;
            Intent intent = new Intent(activity, ActivityCategoryDetails.class);
            intent.putExtra(Constant.EXTRA_OBJC, obj);
            startActivity(intent);

            if (activity instanceof MainActivity mainActivity) {
                mainActivity.showInterstitialAd();
                mainActivity.destroyBannerAd();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapterCategory.resetListData();
            requestAction();
        });

        requestAction();

        return rootView;
    }

    private void displayApiResult(final List<Category> categories) {
        List<Category> safeCategories = (categories != null) ? categories : new ArrayList<>();
        adapterCategory.setListData(safeCategories);
        swipeProgress(false);
        if (safeCategories.isEmpty()) {
            showNoItemView(true);
        }
    }

    private void requestCategoriesApi() {
        ApiInterface apiInterface = RestAdapter.createAPI(sharedPref.getBaseUrl());
        callbackCall = apiInterface.getAllCategories(AppConfig.REST_API_KEY);
        callbackCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<CallbackCategories> call, @NonNull Response<CallbackCategories> response) {
                if (!isAdded() || getActivity() == null) return;

                CallbackCategories resp = response.body();
                if (resp != null && resp.status.equals("ok")) {
                    displayApiResult(resp.categories);
                } else if (resp != null && resp.status.equals("error")) {
                    showInvalidRequest(true);
                    Log.d(Tag.RETROFIT, " Invalid: " + response.message());
                } else {
                    onFailRequest();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CallbackCategories> call, @NonNull Throwable t) {
                if (!isAdded() || getActivity() == null) return;

                if (!call.isCanceled()) {
                    onFailRequest();
                }
            }
        });
    }

    private void onFailRequest() {
        swipeProgress(false);
        if (!isAdded()) return;

        Context ctx = getContext();
        if (ctx != null && Tools.isConnect(ctx)) {
            showFailedView(true, getString(R.string.msg_no_network));
        } else {
            showFailedView(true, getString(R.string.msg_offline));
        }
    }

    private void requestAction() {
        showFailedView(false, "");
        showInvalidRequest(false);
        showNoItemView(false);
        swipeProgress(true);

        if (rootView != null) {
            rootView.post(() -> {
                if (isAdded()) {
                    requestCategoriesApi();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        swipeProgress(false);
        if (callbackCall != null && callbackCall.isExecuted()) {
            callbackCall.cancel();
        }
        if (lytShimmer != null) {
            lytShimmer.stopShimmer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callbackCall != null && !callbackCall.isCanceled()) {
            callbackCall.cancel();
        }
    }

    private void showFailedView(boolean flag, String message) {
        if (rootView == null) return;
        View lyt_failed = rootView.findViewById(R.id.lyt_failed_category);
        ((TextView) rootView.findViewById(R.id.failed_message)).setText(message);
        if (flag) {
            recyclerView.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        rootView.findViewById(R.id.failed_retry).setOnClickListener(view -> requestAction());
    }

    private void showNoItemView(boolean show) {
        if (rootView == null) return;
        View lyt_no_item = rootView.findViewById(R.id.lyt_no_item_category);
        ((TextView) rootView.findViewById(R.id.no_item_message)).setText(R.string.msg_no_category);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
    }

    private void showInvalidRequest(boolean show) {
        swipeProgress(false);
        if (rootView == null) return;
        View lytInvalidLicense = rootView.findViewById(R.id.lyt_invalid_license);
        Button btnPurchaseNow = rootView.findViewById(R.id.btn_purchase_now);
        if (show) {
            lytInvalidLicense.setVisibility(View.VISIBLE);
            btnPurchaseNow.setOnClickListener(view -> {
                if (activity != null) Tools.purchaseItem(activity);
            });
        } else {
            lytInvalidLicense.setVisibility(View.GONE);
        }
    }

    private void swipeProgress(final boolean show) {
        if (swipeRefreshLayout == null || lytShimmer == null) return;
        if (!show) {
            swipeRefreshLayout.setRefreshing(false);
            lytShimmer.setVisibility(View.GONE);
            lytShimmer.stopShimmer();
            return;
        }
        swipeRefreshLayout.post(() -> {
            if (isAdded() && swipeRefreshLayout != null && lytShimmer != null) {
                swipeRefreshLayout.setRefreshing(true);
                lytShimmer.setVisibility(View.VISIBLE);
                lytShimmer.startShimmer();
            }
        });
    }

}