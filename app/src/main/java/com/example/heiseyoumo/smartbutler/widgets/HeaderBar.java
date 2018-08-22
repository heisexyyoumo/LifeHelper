package com.example.heiseyoumo.smartbutler.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.heiseyoumo.smartbutler.R;

public class HeaderBar extends RelativeLayout implements View.OnClickListener {

    private ImageView mMenuIv;
    private ClickCallBack clickCallBack;

    public HeaderBar(Context context) {
        super(context);
        initView(context);
    }

    public HeaderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_head_bar,
                this, true);
        mMenuIv = (ImageView)view.findViewById(R.id.mMenuIv);

        mMenuIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mMenuIv:
                clickCallBack.onMenuClick();
                break;
        }
    }

    //回调接口
    public interface ClickCallBack{
        void onMenuClick();
    }

    public void setClickCallBack(ClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public ImageView getMenuIv(){
        return mMenuIv;
    }
}
