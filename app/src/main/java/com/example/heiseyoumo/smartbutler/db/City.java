package com.example.heiseyoumo.smartbutler.db;

/**
 * 市的实体类
 */

import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

public class City extends LitePalSupport {

    private int id;
    //市的名字
    private String cityName;
    //市的代号
    private int cityCode;
    //市所属省的id
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
