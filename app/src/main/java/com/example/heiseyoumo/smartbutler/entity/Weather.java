package com.example.heiseyoumo.smartbutler.entity;

/**
 * 天气数据weather的实体类
 */
public class Weather {

    //请求是否成功
    private String status;

    //城市的天气id
    private String cityWeatherId;

    //城市名
    private String cityName;
    //天气预报更新时间
    private String updateTime;
    //现在的温度
    private String degree;
    //现在的天气情况，如“晴”，“阵雨”等
    private String weatherInfo;


    //AQI指数
    private String aqi;
    //PM2.5指数
    private String pm25;

    //舒适度
    private String comfort;
    //洗车指数
    private String carWash;
    //运动建议
    private String sport;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }


    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getComfort() {
        return comfort;
    }

    public void setComfort(String comfort) {
        this.comfort = comfort;
    }

    public String getCarWash() {
        return carWash;
    }

    public void setCarWash(String carWash) {
        this.carWash = carWash;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCityWeatherId() {
        return cityWeatherId;
    }

    public void setCityWeatherId(String cityWeatherId) {
        this.cityWeatherId = cityWeatherId;
    }

}
