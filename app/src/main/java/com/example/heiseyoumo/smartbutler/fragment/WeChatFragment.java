package com.example.heiseyoumo.smartbutler.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.adapter.WeChatAdapter;
import com.example.heiseyoumo.smartbutler.entity.WeChatData;
import com.example.heiseyoumo.smartbutler.ui.WebViewActivity;
import com.example.heiseyoumo.smartbutler.utils.L;
import com.example.heiseyoumo.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class WeChatFragment extends Fragment {


    //下拉刷新
    private SwipeRefreshLayout sr_refresh;

    private ListView mListView;
    private List<WeChatData> mList = new ArrayList<>();
    //标题
    private List<String> mListTitle = new ArrayList<>();
    //地址
    private List<String> mListUrl = new ArrayList<>();

    private WeChatAdapter adapter;

    public static final int REFRESH = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_we_chat,null);
        findView(view);
        return view;
    }

    //初始化view
    private void findView(View view) {
        mListView = (ListView)view.findViewById(R.id.mListView);
        sr_refresh = (SwipeRefreshLayout)view.findViewById(R.id.sr_refresh);
        //刷新进度条的颜色
        sr_refresh.setColorSchemeResources(R.color.colorAccent);
        //下拉刷新的监听事件
        sr_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新
                refresh();
            }
        });

        //请求数据，并初始化
        initData();

        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //L.i("position" + position);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                //intent的两种方法传值
                intent.putExtra("title",mListTitle.get(position));
                intent.putExtra("url",mListUrl.get(position));
                startActivity(intent);

            }
        });
    }

    private void initData() {
        //解析接口
        String url = "http://v.juhe.cn/toutiao/index?type=top&key="
                + StaticClass.NEWS_KEY + "&ps=100";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //L.i("json" + t);
                parsingJson(t);
            }
        });
    }

    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //不能再子线程更新UI操作，需要用handler进行一步处理
                Message message = new Message();
                message.what = REFRESH;
                handler.sendMessage(message);

            }
        }).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case REFRESH:
                    initData();
                    adapter.notifyDataSetChanged();
                    sr_refresh.setRefreshing(false);
                    //Toast.makeText(getActivity(),"refresh",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void parsingJson(String t) {
        mList.clear();
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonResult.getJSONArray("data");
            for(int i = 0 ;i < jsonArray.length(); i++){
                JSONObject json = (JSONObject)jsonArray.get(i);
                WeChatData data = new WeChatData();

                String title = json.getString("title");
                String url = json.getString("url");

                data.setTitle(title);
                data.setSource(json.getString("author_name"));
                data.setImgUrl(json.getString("thumbnail_pic_s"));
                data.setDate(json.getString("date"));
                mList.add(data);

                mListTitle.add(title);
                mListUrl.add(url);
            }
            adapter = new WeChatAdapter(getActivity(),mList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
