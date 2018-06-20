package com.aidan.basetools.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aidan.basetools.R;
import com.aidan.basetools.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class DialogMultiChooser<T> extends Dialog {
    public interface NameObject<T>{
        String getName();
        T getData();
    }
    public interface AdapterClick<T>{
        void onClick(T data);
    }

    View rootView;
    Adapter<T> adapter;
    TextView titleTextView;
    ListView dataListView;
    public DialogMultiChooser(@NonNull Context context, List<NameObject<T>> data, AdapterClick<T> adapterClick) {
        super(context);
        this.adapter = new Adapter<>(data,adapterClick);
    }

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.dialog_multi_chooser);
        findView();
        setViewClick();
        setViewValue();
    }

    private void findView() {
        titleTextView = findViewById(R.id.titleTextView);
        dataListView = findViewById(R.id.dataListView);
        rootView = findViewById(R.id.rootView);
    }

    private void setViewClick(){

    }

    private void setViewValue(){
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        params.width = DeviceUtils.getInstance(getContext()).SCREEN_WIDTH * 8 / 10;
        rootView.setLayoutParams(params);
    }


    private class Adapter<T> extends BaseAdapter{
        List<NameObject<T>> data = new ArrayList<>();
        AdapterClick<T> adapterClick;
        public Adapter(List<NameObject<T>> data, AdapterClick<T> adapterClick){
            this.data.addAll(data);
            this.adapterClick = adapterClick;
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public T getItem(int position) {
            return data.get(position).getData();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null || !(convertView instanceof TextView)) {
                convertView = new TextView(parent.getContext());
            }
            ((TextView)convertView).setText(data.get(position).getName());
            if(adapterClick != null){
                convertView.setOnClickListener( v -> adapterClick.onClick(data.get(position).getData()));
            }
            return convertView;
        }
    }
}
