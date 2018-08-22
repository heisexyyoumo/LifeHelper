package com.example.heiseyoumo.smartbutler.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heiseyoumo.smartbutler.R;

public class HeadToolBar extends RelativeLayout implements View.OnClickListener {

    private ImageView mBackIv;
    private TextView mSaveEt;
    private ClickCallBack clickCallBack;

    public HeadToolBar(Context context) {
        super(context);
        initView(context);
    }

    public HeadToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_tool_bar,
                this, true);
        mBackIv = (ImageView)view.findViewById(R.id.mBackIv);
        mSaveEt = (TextView)view.findViewById(R.id.mSaveEt);
        mBackIv.setOnClickListener(this);
        mSaveEt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBackIv:
                clickCallBack.onBackClick();
                break;

            case R.id.mSaveEt:
                clickCallBack.onSaveClick();
                break;
        }
    }

    //回调接口
    public interface ClickCallBack{
        void onBackClick();
        void onSaveClick();

    }

    public void setClickCallBack(ClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public ImageView getBackIv(){
        return mBackIv;
    }

    public TextView getSaveEt(){
        return mSaveEt;
    }
}
