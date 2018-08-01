package com.aidan.basetools.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aidan.basetools.R;
import com.aidan.basetools.utils.DeviceUtils;


/**
 * 有兩種使用方式
 * 1.call api之後不立刻改checked狀態，setSwitchOnListener，setSwitchOffListener，
 * Listener裡去call api，callback後再決定要不要setChecked
 * 2.直接用，點了一定change checked狀態，然後之後再用isChecked()判斷
 */
public class SwitchCompat extends RelativeLayout {

    public ImageView switchBackOnImageView, switchThumbImageView;
    public RelativeLayout container;
    private OnClickListener switchOnListener, switchOffListener;
    private boolean checked = false;
    private OnCheckedChangeListener onCheckedChangeListener = null;
    private OnClickListener defaultSwitchOn = new OnClickListener() {
        @Override
        public void onClick(View view) {
            setChecked(true);
            SwitchCompat.this.setOnClickListener(defaultSwitchOff);


        }
    };
    private OnClickListener defaultSwitchOff = new OnClickListener() {
        @Override
        public void onClick(View view) {
            setChecked(false);
            SwitchCompat.this.setOnClickListener(defaultSwitchOn);

        }
    };


    public SwitchCompat(Context context) {
        super(context);
        init(context);
    }

    public SwitchCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.switchCompat,0,0);

        try {
            int switchOnBgDrawableRes = a.getResourceId(R.styleable.switchCompat_switchOnBgDrawableRes, 0);
            if (switchOnBgDrawableRes != 0) {
                switchBackOnImageView.setImageResource(switchOnBgDrawableRes);
            }



        } catch (Exception e){

        }
        finally {
            a.recycle();
        }


    }

    public SwitchCompat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);

    }


    protected void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.switch_compat, this);
        switchBackOnImageView = findViewById(R.id.switchBackOnImageView);
        switchThumbImageView = findViewById(R.id.switchThumbImageView);
        if (checked) {
            setOnClickListener(defaultSwitchOff);
        } else {
            setOnClickListener(defaultSwitchOn);
        }

    }

    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    public void setChecked(boolean checked, boolean notify) {
        if (this.checked != checked && onCheckedChangeListener != null && notify) {
            onCheckedChangeListener.onCheckedChanged(this, checked);
        }
        this.checked = checked;
        if (checked) {
            //    switchToRight
            switchBackOnImageView.animate().alpha(0.3f);
            switchBackOnImageView.animate().alpha(0.6f);
            switchBackOnImageView.animate().alpha(1.0f);
            switchThumbImageView.animate().translationX(DeviceUtils.getInstance(getContext()).dpToPixel(18.0f));
            if (switchOffListener != null)
                this.setOnClickListener(switchOffListener);
        } else {
            //    switchToLeft
            switchBackOnImageView.animate().alpha(1.0f);
            switchBackOnImageView.animate().alpha(0.6f);
            switchBackOnImageView.animate().alpha(0.3f);
            switchBackOnImageView.animate().alpha(0.0f);
            switchThumbImageView.animate().translationX(0.0f);
            if (switchOnListener != null)
                this.setOnClickListener(switchOnListener);

        }
    }

    public void toggle() {
        setChecked(!checked);
    }


    public void setSwitchOnListener(OnClickListener switchOnListener) {
        this.switchOnListener = switchOnListener;
    }

    public void setSwitchOffListener(OnClickListener switchOffListener) {
        this.switchOffListener = switchOffListener;

    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;

    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(SwitchCompat switchCompat, boolean isChecked);
    }

    public boolean isChecked() {
        return checked;
    }
}

