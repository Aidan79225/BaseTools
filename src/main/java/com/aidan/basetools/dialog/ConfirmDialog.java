package com.aidan.basetools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.aidan.basetools.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s352431 on 2017/3/4.
 */

public class ConfirmDialog extends Dialog {
    private TextView titleTextView, messageTextView, cancelTextView, okTextView;
    private View buttonDivideView;
    private LinearLayout contentLinearLayout, buttonContainerLinearLayout;
    private String message = "";
    private String title = "";
    private View.OnClickListener okClick;
    private View.OnClickListener cancelClick;
    private List<Pair<View, LinearLayout.LayoutParams>> contentViewList = new ArrayList<>();

    private int titleVisibility = View.VISIBLE;
    private int cancelVisibility = View.VISIBLE;
    private int buttonDivideVisibility = View.VISIBLE;
    private int messageTextVisibility = View.VISIBLE;
    private int buttonContainerVisibility = View.VISIBLE;
    private int titleGravity = Gravity.START;
    private int messageGravity = Gravity.START;
    private float messageTextSize = 20f;
    private String okText = "確認";
    private String cancelText = "取消";
    private int cancelTextColorId = 0;

    public ConfirmDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);
    }

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        findView();
        setView();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOkClick(View.OnClickListener okClick) {
        this.okClick = okClick;
    }

    public void setCancelClick(View.OnClickListener cancelClick) {
        this.cancelClick = cancelClick;
    }

    public void setTitleGravity(int gravity) {
        titleGravity = gravity;
    }

    public void setMessageGravity(int gravity) {
        messageGravity = gravity;
    }

    public void hideTitleTextView() {
        titleVisibility = View.GONE;
    }

    public void hideCancelTextView() {
        cancelVisibility = View.GONE;
        buttonDivideVisibility = View.GONE;
    }

    public void hideMessageTextView() {
        messageTextVisibility = View.GONE;
    }

    public void hideButtonContainer() {
        buttonContainerVisibility = View.GONE;
    }

    public void addContentView(View view) {
        Pair<View, LinearLayout.LayoutParams> pair = new Pair<>(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentViewList.add(pair);
    }

    public void addContentView(View view, LinearLayout.LayoutParams params) {
        Pair<View, LinearLayout.LayoutParams> pair = new Pair<>(view, params);
        contentViewList.add(pair);
    }


    public void setMessageTextSize(float size) {
        this.messageTextSize = size;
    }

    public void setOkText(String okText) {
        this.okText = okText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public void setCancelTextColorId(int cancelTextColorId) {
        this.cancelTextColorId = cancelTextColorId;
    }

    private void findView() {
        messageTextView = findViewById(R.id.messageTextView);
        cancelTextView = findViewById(R.id.cancelTextView);
        okTextView = findViewById(R.id.okTextView);
        titleTextView = findViewById(R.id.titleTextView);
        buttonDivideView = findViewById(R.id.buttonDivideView);
        contentLinearLayout = findViewById(R.id.contentLinearLayout);
        buttonContainerLinearLayout = findViewById(R.id.buttonContainerLinearLayout);
    }

    private void setView() {
        setViewVisibility();
        setViewClick();
        setViewContent();
    }

    private void setViewVisibility() {
        titleTextView.setVisibility(titleVisibility);
        cancelTextView.setVisibility(cancelVisibility);
        buttonDivideView.setVisibility(buttonDivideVisibility);
        messageTextView.setVisibility(messageTextVisibility);
        buttonContainerLinearLayout.setVisibility(buttonContainerVisibility);
    }

    private void setViewClick() {
        cancelTextView.setOnClickListener(cancelClick);
        okTextView.setOnClickListener(okClick);
    }

    private void setViewContent() {
        titleTextView.setGravity(titleGravity);
        titleTextView.setText(title);
        messageTextView.setText(message);
        messageTextView.setTextSize(messageTextSize);
        messageTextView.setGravity(messageGravity);
        if (okText.length() > 0) okTextView.setText(okText);
        if (cancelText.length() > 0) cancelTextView.setText(cancelText);
        for (Pair<View, LinearLayout.LayoutParams> pair : contentViewList) {
            contentLinearLayout.addView(pair.first, pair.second);
        }
        if(cancelTextColorId != 0){
            cancelTextView.setTextColor(getContext().getResources().getColor(cancelTextColorId));
        }
    }
}
