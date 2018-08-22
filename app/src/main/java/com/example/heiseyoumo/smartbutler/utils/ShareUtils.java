package com.example.heiseyoumo.smartbutler.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

//实现sharedpreferences的封装
public class ShareUtils {

    public static final String NAME = "config";

    //键 值
    public static void putString(Context mContext,String key,String value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,0);
        sp.edit().putString(key,value).commit();
    }
    //键 默认值
    public static String getString(Context mContext,String key,String value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,0);
        return sp.getString(key,value);
    }
    //键 值
    public static void putInt(Context mContext,String key,int value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,0);
        sp.edit().putInt(key,value).commit();
    }
    //键 默认值
    public static int getInt(Context mContext,String key,int value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,0);
        return sp.getInt(key,value);
    }
    //键 值
    public static void putBoolean(Context mContext,String key,boolean value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,0);
        sp.edit().putBoolean(key,value).commit();
    }
    //键 默认值
    public static boolean getBoolean(Context mContext,String key,boolean value){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,0);
        return sp.getBoolean(key,value);
    }

    //删除单个
    public static void deleShare(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,0);
        sp.edit().remove(key).commit();
    }
    //删除全部
    public static void deleAll(Context mContext,String key){
        SharedPreferences sp = mContext.getSharedPreferences(NAME,0);
        sp.edit().clear().commit();
    }


}
