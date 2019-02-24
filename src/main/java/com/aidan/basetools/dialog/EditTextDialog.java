package com.aidan.basetools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aidan.basetools.R;
import com.aidan.basetools.utils.DeviceUtils;

import androidx.annotation.NonNull;

public class EditTextDialog extends Dialog {
    private ViewGroup rootView;
    private TextView cancelTextView;
    private TextView okTextView;
    private TextView titleTextView;
    private TextView remindTextView;
    private EditText messageEditText;
    private Button clearButton;

    private String titleText = "提示";
    private String okText = "確認";
    private String cancelText = "取消";

    private String remindText = "請輸入提示字元";

    private OkClickListener okClickListener;

    private View.OnClickListener cancelClickListener;
    private Runnable cancelCallback;


    public interface OkClickListener{
        void onClick(String msg);
    }

    public EditTextDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.dialog_edit_text);
        findView();
        setViewClick();
        setViewValue();
    }

    private void findView() {
        rootView = findViewById(R.id.rootView);
        cancelTextView = findViewById(R.id.cancelTextView);
        okTextView = findViewById(R.id.okTextView);
        titleTextView = findViewById(R.id.titleTextView);
        messageEditText = findViewById(R.id.messageEditText);
        clearButton = findViewById(R.id.clearButton);
        remindTextView = findViewById(R.id.remindTextView);
    }

    private void setViewClick() {
        cancelTextView.setOnClickListener(cancelClickListener);
        okTextView.setOnClickListener(v -> {
            if(okClickListener != null){
                okClickListener.onClick(messageEditText.getText().toString());
            }
            dismiss();
        });
        clearButton.setOnClickListener(v -> messageEditText.setText(""));
        setOnCancelListener(dialog -> {
            if(cancelCallback != null){
                cancelCallback.run();
            }
            dialog.dismiss();
        });
    }

    private void setViewValue(){
        remindTextView.setText(remindText);
        titleTextView.setText(titleText);
        messageEditText.setText("");
        okTextView.setText(okText);
        cancelTextView.setText(cancelText);
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        params.width = DeviceUtils.getInstance(getContext()).SCREEN_WIDTH * 8 / 10;
        rootView.setLayoutParams(params);
    }

    public EditTextDialog setOkClickListener(OkClickListener okClickListener) {
        this.okClickListener = okClickListener;
        return this;
    }

    public EditTextDialog setCancelClickListener(View.OnClickListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
        return this;
    }

    public EditTextDialog setCancelCallback(Runnable cancelCallback) {
        this.cancelCallback = cancelCallback;
        return this;
    }

    public EditTextDialog setTitleText(String titleText) {
        this.titleText = titleText;
        return this;
    }

    public EditTextDialog setOkText(String okText) {
        this.okText = okText;
        return this;
    }

    public EditTextDialog setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    public EditTextDialog setRemindText(String remindText) {
        this.remindText = remindText;
        return this;
    }
}


