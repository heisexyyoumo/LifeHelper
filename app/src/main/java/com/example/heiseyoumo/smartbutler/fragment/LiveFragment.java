package com.example.heiseyoumo.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;

import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.adapter.LiveAdapter;
import com.example.heiseyoumo.smartbutler.ui.LiveTvActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.heiseyoumo.smartbutler.R.anim.layout_animation_fall_down;

public class LiveFragment extends Fragment {

    //RecycleView
    private RecyclerView mLiveRv;
    private LiveAdapter adapter;
    private List<String> mNameList = new ArrayList<>();
    private List<String> mUrlList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_live, null);
        findView(view);
        initData();
//        clickListener();
        return view;

    }

    private void findView(View view) {
        mLiveRv = (RecyclerView) view.findViewById(R.id.mLiveRv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mLiveRv.setLayoutManager(layoutManager);
        adapter = new LiveAdapter(mNameList,mUrlList);
        mLiveRv.setAdapter(adapter);
    }

    private void initData() {
        String[] name = {"海贼王", "爱情公寓", "唐人电视","澳亚卫视","环球电视台",
                    "朝鲜日报台","沙特阿拉伯","韩国中央台","KCTV","美国中文电视"};
        String[] url = {"http://dlhls.cdn.zhanqi.tv/zqlive/37119_4ibXM.m3u8",
                "http://dlhls.cdn.zhanqi.tv/zqlive/34338_PVMT5.m3u8",
                "http://174.127.67.246/live200/playlist.m3u8",
                "http://stream.mastvnet.com/MASTV/sd/live.m3u8",
                "http://live-cdn.xzxwhcb.com/hls/sn88wrar.m3u8",
                "rtmp://live.chosun.gscdn.com:1935/live/tvchosun1.stream",
                "http://stream02.fasttelco.net:1935/live/_definst_/alrai.stream/lihattv.m3u8",
                "http://122.202.129.136:1935/live/ch5/playlist.m3u8",
                "rtmp://hk2.hwadzan.com/liveedge/amtb",
                "rtsp://c.itvitv.com/mnet.sdha."};

        //将数据装入List中
        mNameList.addAll(Arrays.asList(name));
        mUrlList.addAll(Arrays.asList(url));
    }

//    private void clickListener() {
//        adapter.setOnItemClickListener(new LiveAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(view.getContext(), LiveTvActivity.class);
//                intent.putExtra("url", mUrlList.get(position));
//                intent.putExtra("title", mNameList.get(position));
//                startActivity(intent);
//            }
//        });
//    }


}
