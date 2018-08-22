package com.example.heiseyoumo.smartbutler.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.entity.Forecast;
import com.example.heiseyoumo.smartbutler.entity.Weather;
import com.example.heiseyoumo.smartbutler.service.AutoUpdateService;
import com.example.heiseyoumo.smartbutler.utils.JsonUtil;
import com.example.heiseyoumo.smartbutler.utils.LogUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.squareup.picasso.Picasso;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "WeatherActivity";

    private ScrollView sv_weather;

    private TextView tv_city;
    private TextView mBackTv;

    private TextView tv_degree;
    private TextView tv_weather_info;

    private LinearLayout ll_forecast;

    private TextView tv_aqi;
    private TextView tv_pm25;

    private TextView tv_comfort;
    private TextView tv_car_wash;
    private TextView tv_sport;

    //获取bing的每日一图做背景图片
    private ImageView iv_img;

    //下拉刷新
    public SwipeRefreshLayout sr_refresh;
    //所属城市的天气id
    private String cityWeatherId;

    //滑动菜单
    public DrawerLayout dl_select;
    //选择按钮
    private Button btn_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        initView();
    }

    //初始化各控件
    private void initView() {

        sv_weather = (ScrollView)findViewById(R.id.sv_weather);

        tv_city = (TextView)findViewById(R.id.tv_city);
        mBackTv = (TextView)findViewById(R.id.mBackTv);
        mBackTv.setOnClickListener(this);

        tv_degree = (TextView)findViewById(R.id.tv_degree);
        tv_weather_info = (TextView)findViewById(R.id.tv_weather_info);

        ll_forecast = (LinearLayout)findViewById(R.id.ll_forecast);

        tv_aqi = (TextView)findViewById(R.id.tv_aqi);
        tv_pm25 = (TextView)findViewById(R.id.tv_pm25);

        tv_comfort = (TextView)findViewById(R.id.tv_comfort);
        tv_car_wash = (TextView)findViewById(R.id.tv_car_wash);
        tv_sport = (TextView)findViewById(R.id.tv_sport);

        //每日一图初始化
        iv_img = (ImageView)findViewById(R.id.iv_img);
        //下拉刷新初始化
        sr_refresh = (SwipeRefreshLayout)findViewById(R.id.sr_refresh);
        //设置下拉刷新进度条的颜色
        sr_refresh.setColorSchemeResources(R.color.colorAccent);

        //初始化滑动菜单
        dl_select = (DrawerLayout)findViewById(R.id.dl_select);
        //初始化选择按钮
        btn_select = (Button)findViewById(R.id.btn_select);

        //SwipeRefreshLayout的监听事件
        sr_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(cityWeatherId);

            }
        });

        //选择按钮btn_select的监听事件
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开滑动菜单
                dl_select.openDrawer(GravityCompat.START);
            }
        });

        //获取SharedPreferences的实例
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);

        if (weatherString != null){
            //有缓存直接解析天气数据

            Weather weather = JsonUtil.parsingWeatherResponse(weatherString);
            //获得所属城市的天气id
            cityWeatherId = weather.getCityWeatherId();
            showWeatherInfo(weather);
        }else {
            //无缓存去服务器查询天气

            cityWeatherId = getIntent().getStringExtra("weather_id");
            //去服务器请求数据现将ScrollView隐藏，不然空数据的界面看上去不好看
            sv_weather.setVisibility(View.INVISIBLE);
            requestWeather(cityWeatherId);
        }

        String bingPic = prefs.getString("bing_pic",null);
        if (bingPic != null){
//            Glide.with(this).load(bingPic).into(iv_img);
            Picasso.with(this).load(bingPic).into(iv_img);
        }else {
            loadBingPic();
        }

    }

    //加载图片
    private void loadBingPic() {
        String bingUrl = "http://guolin.tech/api/bing_pic";
        RxVolley.get(bingUrl, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                final String bingPic = t;
                SharedPreferences.Editor editor =
                        PreferenceManager.getDefaultSharedPreferences
                                (WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        Glide.with(WeatherActivity.this).load(bingPic).into(iv_img);
                        Picasso.with(WeatherActivity.this).load(bingPic).into(iv_img);
                    }
                });
            }

        });

    }

    //拼接url
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +
                "&key=72e91c1c18ea4e7292ca877c58835a90";

        RxVolley.get(weatherUrl, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                final String responseText = t;
                final Weather weather = JsonUtil.parsingWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && JsonUtil.mList.size() > 0
                                && weather.getStatus().equals("ok")){
                            //获取SharedPreferences.Editor的实例
                            SharedPreferences.Editor editor =
                                    PreferenceManager.getDefaultSharedPreferences
                                            (WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            //获得所属城市的天气id
                            cityWeatherId = weather.getCityWeatherId();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,
                                    "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        //隐藏进度条
                        sr_refresh.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onFailure(VolleyError error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,
                                "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        //隐藏进度条
                        sr_refresh.setRefreshing(false);
                    }
                });
            }
        });

        loadBingPic();
    }


    /**
     * 将请求得到的数据装入对应的布局中
     */
    private void showWeatherInfo(Weather weather) {

        tv_city.setText(weather.getCityName());

        tv_degree.setText(weather.getDegree() + "°C");
        tv_weather_info.setText(weather.getWeatherInfo());

        tv_aqi.setText(weather.getAqi());
        tv_pm25.setText(weather.getPm25());

        tv_comfort.setText("舒适度：" + weather.getComfort());
        tv_car_wash.setText("洗车指数：" + weather.getCarWash());
        tv_sport.setText("运动建议：" + weather.getSport());

        //清空，防止重复
        ll_forecast.removeAllViews();
        for (int i = 0; i < JsonUtil.mList.size(); i++){
            View view = LayoutInflater.from(this).inflate
                    (R.layout.forecast_item,ll_forecast,false);

            Forecast forecast = JsonUtil.mList.get(i);

            LogUtil.w(TAG,"date : " + forecast.getForecastDate());
            TextView tv_date = (TextView)view.findViewById(R.id.tv_date);
            TextView tv_info = (TextView)view.findViewById(R.id.tv_info);
            TextView tv_max = (TextView)view.findViewById(R.id.tv_max);
            TextView tv_min = (TextView)view.findViewById(R.id.tv_min);

            tv_date.setText(forecast.getForecastDate());
            tv_info.setText(forecast.getForecastWeather());
            tv_max.setText(forecast.getForecastMax() + "°C");
            tv_min.setText(forecast.getForecastMin() + "°C");

            ll_forecast.addView(view);
        }

        sv_weather.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBackTv:
                finish();
                break;
        }
    }
}
