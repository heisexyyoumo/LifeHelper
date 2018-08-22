package com.example.heiseyoumo.smartbutler.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.utils.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePal;

import cn.bmob.v3.Bmob;
import io.vov.vitamio.Vitamio;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);
        //初始化bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);
        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=" + StaticClass.VOICE_KEY);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        //初始化LitePal
        LitePal.initialize(this);
        //初始化Vitamio
        Vitamio.isInitialized(this);

    }
}
