package com.app.androidnewsapp.util;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.app.androidnewsapp.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

public class MaterialLoadingDialog {

    private AlertDialog dialog;
    private Activity activity;
    private MaterialTextView tvMessage;

    public MaterialLoadingDialog(Activity activity) {
        this.activity = activity;
        setupDialog();
    }

    private void setupDialog() {
        // Inflate layout M3
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_loading, null);
        tvMessage = view.findViewById(R.id.tv_message);

        // Buat dialog menggunakan MaterialAlertDialogBuilder
        dialog = new MaterialAlertDialogBuilder(activity)
                .setView(view)
                .setCancelable(false)
                .create();
    }

    public void show(String message) {
        if (activity == null || activity.isFinishing()) return;

        if (message != null && !message.isEmpty()) {
            tvMessage.setText(message);
        }

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}