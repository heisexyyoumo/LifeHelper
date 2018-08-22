package com.example.heiseyoumo.smartbutler.fragment;

/**
 * 加载数据的fragment，依赖于MainActivity
 * Tips：当有数据从数据库读出时，若修改数据库后，重新运行前应先卸载app
 */


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heiseyoumo.smartbutler.MainActivity;
import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.db.City;
import com.example.heiseyoumo.smartbutler.db.County;
import com.example.heiseyoumo.smartbutler.db.Province;
import com.example.heiseyoumo.smartbutler.ui.ChooseWeatherActivity;
import com.example.heiseyoumo.smartbutler.ui.WeatherActivity;
import com.example.heiseyoumo.smartbutler.utils.JsonUtil;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView tv_title;
    private Button btn_back;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private List<String> mList = new ArrayList<>();
    //省列表
    private List<Province> provinceList;
    //市列表
    private List<City> cityList;
    //县列表
    private List<County> countyList;
    //选择的省份
    private Province selectedProvince;
    //选择的城市
    private City selectedCity;
    //当前选中的级别
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container,false);
        findView(view);
        return view;

    }

    //初始化布局
    private void findView(View view) {
        tv_title = (TextView)view.findViewById(R.id.tv_title);
        mListView = (ListView)view.findViewById(R.id.mListView);
        btn_back = (Button)view.findViewById(R.id.btn_back);
        adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1,mList);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if (currentLevel == LEVEL_COUNTY){
                    //如果当前级别是LEVEL_COUNTY就启动WeatherActivity，并把县的天气id传过去

                    String weatherId = countyList.get(position).getWeatherId();
                    if (getActivity()instanceof ChooseWeatherActivity){
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id",weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity)getActivity();
                        activity.dl_select.closeDrawers();
                        activity.sr_refresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }

                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }

            }
        });

        queryProvinces();
    }

    //查询全国所有的省，优先从数据库查询，如果没有在去服务器上查询
    private void queryProvinces() {
        tv_title.setText("中国");
        btn_back.setVisibility(View.GONE);
        provinceList = LitePal.findAll(Province.class);
        if (provinceList.size() > 0 ){
//            LogUtil.w("MainActivity"," province size = " + provinceList.size());
            mList.clear();
            for (Province province : provinceList){
                mList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String url = "http://guolin.tech/api/china";
            queryFromServer(url,"province");
        }
    }

    //查询省内所有的市，优先从数据库查询，如果没有在去服务器上查询
    private void queryCities() {
        tv_title.setText(selectedProvince.getProvinceName());
        btn_back.setVisibility(View.VISIBLE);
        cityList = LitePal.where("provinceid = ?",
                String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0){
//            LogUtil.w("MainActivity"," city size = " + cityList.size());
            mList.clear();
            for (City city : cityList){
                mList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String url = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(url,"city");
        }
    }

    //查询市内所有的县，优先从数据库查询，如果没有在去服务器上查询
    private void queryCounties() {
        tv_title.setText(selectedCity.getCityName());
        btn_back.setVisibility(View.VISIBLE);
        countyList = LitePal.where("cityid = ?",
                String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0){

            mList.clear();
            for (County county : countyList){
                mList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String url = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(url,"county");
        }
    }

    //根据传入的地址和类型从服务器上查询省市县数据
    private void queryFromServer(String url, final String type) {
        showProgressDialog();
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                boolean result = false;
                //LogUtil.e(t);
                //Toast.makeText(getActivity(), "json :" + t, Toast.LENGTH_SHORT).show();
                if ("province".equals(type)){
                    result = JsonUtil.parsingProvinceResponse(t);
                }else if ("city".equals(type)){
                    result = JsonUtil.parsingCityResponse(t,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = JsonUtil.parsingCountyResponse(t,selectedCity.getId());
                }
                if (result){
                    //通过runOnUiThread()方法回到主线程处理逻辑
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //显示进度对话框
    private void showProgressDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭进度对话框
    private void closeProgressDialog() {
        if (progressDialog != null){
            progressDialog.dismiss();
        }

    }


}
