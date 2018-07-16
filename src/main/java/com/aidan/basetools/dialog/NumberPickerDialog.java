package com.aidan.basetools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.TextView;


import com.aidan.basetools.R;

import java.lang.reflect.Field;

public class NumberPickerDialog extends Dialog {
    private TextView titleTextView,okTextView,cancelTextView;
    private NumberPicker numberPicker;

    private String title = "請選擇數量";

    public interface ConfirmClick{
        void onClick(int number);
    }
    public NumberPickerDialog(@NonNull Context context, int maxValue, int minValue, ConfirmClick confirmClick) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_number_picker);
        findView();
        setView(maxValue, minValue, confirmClick);
    }

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);

    }

    private void findView(){
        titleTextView = findViewById(R.id.titleTextView);
        okTextView = findViewById(R.id.okTextView);
        cancelTextView = findViewById(R.id.cancelTextView);
        numberPicker = findViewById(R.id.numberPicker);
    }

    private void setView(int maxValue, int minValue, ConfirmClick confirmClick){
        titleTextView.setText(title);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setMinValue(minValue);
        setNumberPickerDividerColor(numberPicker, getContext().getResources().getColor(R.color.light_gray_color));
        okTextView.setOnClickListener(v -> {
            if(confirmClick != null){
                confirmClick.onClick(numberPicker.getValue());
            }
            dismiss();
        });
        cancelTextView.setOnClickListener( v -> dismiss());
    }

    private void setNumberPickerDividerColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();

        for (int i = 0; i < count; i++) {

            try {
                Field dividerField = numberPicker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(color);
                dividerField.set(numberPicker, colorDrawable);
                numberPicker.invalidate();
            } catch (Exception e) {
            }
        }
    }

    public NumberPickerDialog setTitle(String title) {
        this.title = title;
        return this;
    }


}
