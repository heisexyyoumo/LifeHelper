package com.example.heiseyoumo.smartbutler.db;

/**
 * 县的实体类
 */

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

public class County extends LitePalSupport {

    private int id;
    //县的名字
    private String countyName;
    //县对应的天气id
    private String weatherId;
    //当前县所属市的id
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
