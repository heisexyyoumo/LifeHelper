package com.example.heiseyoumo.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.adapter.PictureAdapter;
import com.example.heiseyoumo.smartbutler.entity.Fruit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PictureFragment extends Fragment {

    private PictureAdapter mAdapter;
    private RecyclerView mPictureRv;
    private List<Fruit> mList = new ArrayList<>();
    private SwipeRefreshLayout mRefreshSr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_picture, null);
        initFruitCard();
        findView(view);
        return view;
    }

    private void findView(View view) {
        mPictureRv = (RecyclerView) view.findViewById(R.id.mPictureRv);
        mRefreshSr = (SwipeRefreshLayout)view.findViewById(R.id.mRefreshSr);
        mAdapter = new PictureAdapter(getActivity(),mList);

        //设置RecycleView,每一行有两列数据
        GridLayoutManager gm = new GridLayoutManager(view.getContext(), 2);
        mPictureRv.setLayoutManager(gm);
        //加载适配器
        mPictureRv.setAdapter(mAdapter);

        //设置下拉刷新
        mRefreshSr.setColorSchemeResources(R.color.colorAccent);
        mRefreshSr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

    }

    //刷新
    private void refresh() {
        initFruitCard();
        mAdapter.notifyDataSetChanged();
        mRefreshSr.setRefreshing(false);
    }

    //初始化mList的数据
    private void initFruitCard() {
        Fruit[] fruitCards = {new Fruit("Apple", R.drawable.apple),
                new Fruit("Banana", R.drawable.banana),
                new Fruit("Orange", R.drawable.orange),
                new Fruit("Watermelon", R.drawable.watermelon),
                new Fruit("Pear", R.drawable.pear),
                new Fruit("Grape", R.drawable.grape),
                new Fruit("Pineapple", R.drawable.pineapple),
                new Fruit("Strawberry", R.drawable.strawberry),
                new Fruit("Cherry", R.drawable.cherry),
                new Fruit("Mango", R.drawable.mango)};
        mList.clear();
        for (int i = 0; i < 50; i++){
            Random random = new Random();
            int index = random.nextInt(fruitCards.length);
            mList.add(fruitCards[index]);
        }

    }

}
