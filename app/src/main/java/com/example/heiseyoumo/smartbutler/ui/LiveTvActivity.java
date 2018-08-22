package com.example.heiseyoumo.smartbutler.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heiseyoumo.smartbutler.R;

import java.util.Calendar;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

public class LiveTvActivity extends AppCompatActivity implements View.OnClickListener {

    //    private String title;
//    private String url;
//    public static final String TAG = "LiveTvActivity";
    //根布局
    private RelativeLayout mRootRlLayout;
    private RelativeLayout mLoadingRlLayout;
    //顶部panel
    private LinearLayout mTopLlLayout;
    private ImageView mBackIv;
    private TextView mNameTv;
    private TextView mTimeTv;
    //底部panel
    private LinearLayout mBottomLlLayout;
    private ImageView mPlayIv;
    //判断是否继续更新时间
    private boolean stop = false;
    //设置静态防止内存泄露
    private static Handler handler;
    //定时隐藏上下panel
    private static Handler mHandler;

    private VideoView mVideoView;
    //重连次数
    private static final int RETRY_TIME = 5;
    //自动隐藏时间
    private static final int AUTO_HIDE_TIME = 5000;


    private int mCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;
        setContentView(R.layout.activity_live_tv);
        initView();
        loadData();
        listenerEvent();
    }


    private void initView() {
        mRootRlLayout = (RelativeLayout) findViewById(R.id.mRootRlLayout);
        //进度条布局
        mLoadingRlLayout = (RelativeLayout) findViewById(R.id.mLoadingRlLayout);
        //顶部panel
        mTopLlLayout = (LinearLayout) findViewById(R.id.mTopLlLayout);
        mBackIv = (ImageView) findViewById(R.id.mBackIv);
        mNameTv = (TextView) findViewById(R.id.mNameTv);
        mTimeTv = (TextView) findViewById(R.id.mTimeTv);
        //底部panel
        mBottomLlLayout = (LinearLayout) findViewById(R.id.mBottomLlLayout);
        mPlayIv = (ImageView) findViewById(R.id.mPlayIv);

        handler = new Handler();

        mHandler = new Handler(Looper.getMainLooper());

        mVideoView = (VideoView) findViewById(R.id.mVideoView);

    }

    private void loadData() {
        mNameTv.setText(getIntent().getStringExtra("title"));
        //更新系统时间
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setSysTime();
                if (!stop) {
                    handler.postDelayed(this, 1000);
                }
            }
        }, 0);

        mVideoView.setVideoURI(Uri.parse(getIntent().getStringExtra("url")));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.start();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (mCount > RETRY_TIME) {
                    new AlertDialog.Builder(LiveTvActivity.this)
                            .setMessage("播放出错")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LiveTvActivity.this.finish();
                                }
                            }).setCancelable(false).show();
                } else {
                    mVideoView.stopPlayback();
                    mVideoView.setVideoURI(Uri.parse(getIntent().getStringExtra("url")));
                }
                mCount++;
                return false;
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mLoadingRlLayout.setVisibility(View.VISIBLE);
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        mLoadingRlLayout.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

    }

    //获取当前时间并设置当前时间
    @SuppressLint("SetTextI18n")
    private void setSysTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        mTimeTv.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
    }

    private void listenerEvent() {
        mBackIv.setOnClickListener(this);
        mRootRlLayout.setOnClickListener(this);
        mPlayIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBackIv:
                finish();
                break;
            case R.id.mRootRlLayout:
                if (mTopLlLayout.getVisibility() == View.VISIBLE || mBottomLlLayout
                        .getVisibility() == View.VISIBLE) {
                    mTopLlLayout.setVisibility(View.GONE);
                    mBottomLlLayout.setVisibility(View.GONE);
                    return;
                }
                if (mVideoView.isPlaying()) {
                    mPlayIv.setImageResource(R.drawable.icon_play);
                } else {
                    mPlayIv.setImageResource(R.drawable.icon_pause);
                }
                mTopLlLayout.setVisibility(View.VISIBLE);
                mBottomLlLayout.setVisibility(View.VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTopLlLayout.setVisibility(View.GONE);
                        mBottomLlLayout.setVisibility(View.GONE);
                    }
                }, AUTO_HIDE_TIME);
                break;

            case R.id.mPlayIv:
                if (mVideoView.isPlaying()) {
                    mVideoView.stopPlayback();
                    mPlayIv.setImageResource(R.drawable.icon_pause);
                } else {
                    mVideoView.setVideoURI(Uri.parse(getIntent().getStringExtra("url")));
                    mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mVideoView.start();
                        }
                    });
                    mPlayIv.setImageResource(R.drawable.icon_play);
                }
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
        stop = true;

    }
}
