package com.example.heiseyoumo.smartbutler.entity;

/**
 * 天气预报的实体类
 */

public class Forecast {

    //天气预报的日期
    private String forecastDate;
    //天气预报的日期的天气
    private String forecastWeather;
    //天气预报的日期的最高温度
    private String forecastMax;
    //天气预报的日期的最低温度
    private String forecastMin;

    public String getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getForecastWeather() {
        return forecastWeather;
    }

    public void setForecastWeather(String forecastWeather) {
        this.forecastWeather = forecastWeather;
    }

    public String getForecastMax() {
        return forecastMax;
    }

    public void setForecastMax(String forecastMax) {
        this.forecastMax = forecastMax;
    }

    public String getForecastMin() {
        return forecastMin;
    }

    public void setForecastMin(String forecastMin) {
        this.forecastMin = forecastMin;
    }
}
