package com.example.heiseyoumo.smartbutler.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.adapter.ChatListAdapter;
import com.example.heiseyoumo.smartbutler.entity.ChatListData;
import com.example.heiseyoumo.smartbutler.utils.L;
import com.example.heiseyoumo.smartbutler.utils.ShareUtils;
import com.example.heiseyoumo.smartbutler.utils.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class ButlerFragment extends Fragment implements View.OnClickListener {

    private ListView mChatListView;
    private List<ChatListData> mList = new ArrayList<>();
    private ChatListAdapter adapter;
    //输入框
    private EditText et_text;
    //发送按钮
    private Button btn_send;
    //将得到的输入框的内容转码
    private String encode_text;
    //TTS
    private SpeechSynthesizer mTts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler,null);
        findView(view);
        return view;
    }
    //初始化view
    private void findView(View view) {

        mTts= SpeechSynthesizer.createSynthesizer(getActivity(),null);
        mTts.setParameter( SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter( SpeechConstant.SPEED, "50" );
        mTts.setParameter( SpeechConstant.VOICE_NAME, "xiaoyan" );
        mTts.setParameter( SpeechConstant.VOLUME, "80" );

        et_text = (EditText)view.findViewById(R.id.et_text);
        btn_send = (Button)view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        mChatListView =(ListView)view.findViewById(R.id.mChatListView);
        //设置适配器
        adapter = new ChatListAdapter(getActivity(),mList);
        mChatListView.setAdapter(adapter);

        addLeftItem("您好，我是小图！");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                /**
                 * 逻辑
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.清空输入框内容
                 * 4.添加你输入的内容到right_item
                 * 5.将得到的输入框的内容转码utf-8
                 * 6.发送给机器人，请求返回内容
                 * 7.拿到机器人的返回值之后添加到left_item
                 */
                //1.获取输入框的内容
                String text = et_text.getText().toString();
                //2.判断是否为空
                if(!TextUtils.isEmpty(text)){
                    //3.清空输入框内容
                    et_text.setText("");
                    //4.添加你输入的内容到right_item
                    addRightItem(text);
                    //5.将得到的输入框的内容转码utf-8
                    try {

                        encode_text = URLEncoder.encode(text, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //6.发送给机器人，请求返回内容
                    String url = "http://www.tuling123.com/openapi/api?key="
                            + StaticClass.TURING_KEY + "&info=" + encode_text;
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            L.i("json:" + t);
                            parsingJson(t);
                        }
                    });
                }else {
                    Toast.makeText(getActivity(),"输入框不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //解析json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            String text = jsonObject.getString("text");
            //7.拿到机器人的返回值之后添加到left_item
            addLeftItem(text);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftItem(String text){

        Boolean isSpeak = ShareUtils.getBoolean(getActivity(),"isSpeak",false);
        if(isSpeak){
            //左边说话
            startSpeak(text);
        }

        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到最底部
        mChatListView.setSelection(mChatListView.getBottom());
    }
    //添加右边文本
    private void addRightItem(String text){
        ChatListData data = new ChatListData();
        data.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到最底部
        mChatListView.setSelection(mChatListView.getBottom());
    }
    //开始说话
    private void startSpeak(String text){
        mTts.startSpeaking(text,mSynListener);
    }
    private SynthesizerListener mSynListener = new SynthesizerListener() {

        public void onCompleted(SpeechError error) {
        }
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }
        public void onSpeakBegin() {
        }
        public void onSpeakPaused() {
        }
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }
        public void onSpeakResumed() {
        }
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };

}
