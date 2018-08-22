package com.example.heiseyoumo.smartbutler.service;

/**
 * 实现后台每8个小时更新一次天气
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.heiseyoumo.smartbutler.entity.Weather;
import com.example.heiseyoumo.smartbutler.utils.JsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

public class AutoUpdateService extends Service {
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        //8小时的毫秒数
        int time = 8 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent,flags,startId);
    }

    //更新每日一图
    private void updateBingPic() {
        String bingUrl = "http://guolin.tech/api/bing_pic";
        RxVolley.get(bingUrl, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                String bingPic = t;
                SharedPreferences.Editor editor =
                        PreferenceManager.getDefaultSharedPreferences
                                (AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }

        });
    }

    //更新天气信息
    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if (weatherString != null){
            Weather weather = JsonUtil.parsingWeatherResponse(weatherString);
            String weatherId = weather.getCityWeatherId();
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +
                    "&key=72e91c1c18ea4e7292ca877c58835a90";

            RxVolley.get(weatherUrl, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    final String responseText = t;
                    Weather weather = JsonUtil.parsingWeatherResponse(responseText);
                    if (weather != null && JsonUtil.mList.size() > 0
                            && weather.getStatus().equals("ok")){
                        //获取SharedPreferences.Editor的实例
                        SharedPreferences.Editor editor =
                                PreferenceManager.getDefaultSharedPreferences
                                        (AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                }
            });
        }
    }
}
