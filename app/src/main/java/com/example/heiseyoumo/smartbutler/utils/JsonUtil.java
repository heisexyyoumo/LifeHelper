package com.example.heiseyoumo.smartbutler.utils;

/**
 * 解析和处理服务器返回的json数据
 */
import android.text.TextUtils;

import com.example.heiseyoumo.smartbutler.db.City;
import com.example.heiseyoumo.smartbutler.db.County;
import com.example.heiseyoumo.smartbutler.db.Province;
import com.example.heiseyoumo.smartbutler.entity.Forecast;
import com.example.heiseyoumo.smartbutler.entity.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JsonUtil {

    public static List<Forecast> mList = new ArrayList<>();
    public static final String TAG = "WeatherActivity";

    /**
     *解析和处理服务器返回的省级数据
     */
    public static boolean parsingProvinceResponse(String response){
        //判断是否为空
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject json = (JSONObject)jsonArray.get(i);
                    Province province = new Province();
                    //得到省的名字name
                    province.setProvinceName(json.getString("name"));
                    //得到省的代号id
                    province.setProvinceCode(json.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /**
     * 解析和处理服务器返回的市级数据
     */

    public static boolean parsingCityResponse(String response, int provinceId){
        //判断是否为空
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject json = (JSONObject)jsonArray.get(i);
                    City city = new City();
                    //得到市的名字name
                    city.setCityName(json.getString("name"));
                    //得到市的代号id
                    city.setCityCode(json.getInt("id"));
                    //得到市所属的省的id（代号）
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean parsingCountyResponse(String response, int cityId){
        //判断是否为空
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject json = (JSONObject)jsonArray.get(i);
                    County county = new County();
                    //得到县的名字name
                    county.setCountyName(json.getString("name"));
                    //得到县对应的天气id
                    county.setWeatherId(json.getString("weather_id"));
                    //得到县所属的市的id（代号）
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析天气的数据
     */

    public static Weather parsingWeatherResponse(String response){

        mList.clear();
        Weather weather = new Weather();

        try {
            JSONObject jsonObject = new JSONObject(response);

            //****这一行很重要****
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");

            JSONObject jsonObjectResult = jsonArray.getJSONObject(0);

            //解析status
            weather.setStatus(jsonObjectResult.getString("status"));

            //解析basic
            JSONObject jsonBasic = (JSONObject)jsonObjectResult.get("basic");
            weather.setCityName(jsonBasic.getString("city"));
            weather.setCityWeatherId(jsonBasic.getString("id"));

            //解析update
            JSONObject jsonUpdate = (JSONObject)jsonObjectResult.get("update");
            weather.setUpdateTime(jsonUpdate.getString("loc"));

            //解析now
            JSONObject jsonNow = (JSONObject)jsonObjectResult.get("now");
            weather.setDegree(jsonNow.getString("tmp"));
            weather.setWeatherInfo(jsonNow.getString("cond_txt"));

            //解析daily_forecast
            JSONArray jsonForecast = jsonObjectResult.getJSONArray("daily_forecast");
            for (int i = 0; i < jsonForecast.length(); i++){
                JSONObject json = (JSONObject) jsonForecast.get(i);

                //注：解析JSONArray时，类的对象需要在循环里面定义****
                // 不然会出现后面的数据把前面的数据覆盖****
                Forecast forecast = new Forecast();

                forecast.setForecastDate(json.getString("date"));
                LogUtil.w(TAG,"date : " + json.getString("date"));

                //注：json.getJSONObject("cond") 与 (JSONObject)json.get("cond")效果是一样的
                JSONObject jsonCond = json.getJSONObject("cond");
                forecast.setForecastWeather(jsonCond.getString("txt_d"));

                JSONObject jsonTmp = json.getJSONObject("tmp");
                forecast.setForecastMax(jsonTmp.getString("max"));
                forecast.setForecastMin(jsonTmp.getString("min"));
                mList.add(forecast);
            }

            //解析aqi
            JSONObject jsonAqi = (JSONObject)jsonObjectResult.get("aqi");
            JSONObject jsonCity = jsonAqi.getJSONObject("city");
            weather.setAqi(jsonCity.getString("aqi"));
            weather.setPm25(jsonCity.getString("pm25"));

            //解析suggestion
            JSONObject jsonSuggestion = (JSONObject)jsonObjectResult.get("suggestion");
            JSONObject jsonComf = jsonSuggestion.getJSONObject("comf");
            weather.setComfort(jsonComf.getString("txt"));

            JSONObject jsonSport = jsonSuggestion.getJSONObject("sport");
            weather.setSport(jsonSport.getString("txt"));

            JSONObject jsonCw = jsonSuggestion.getJSONObject("cw");
            weather.setCarWash(jsonCw.getString("txt"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;

    }

}
