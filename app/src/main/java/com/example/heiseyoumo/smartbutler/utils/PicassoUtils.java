package com.example.heiseyoumo.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.example.heiseyoumo.smartbutler.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Picasso的封装
 */

public class PicassoUtils {

    //默认加载图片
    public static void loadImageView(Context mContext,String url, ImageView imageView){
        Picasso.with(mContext).load(url).into(imageView);
    }

    //默认加载图片(指定大小)
    public static void loadImageViewSize(Context mContext,String url,int width,int height, ImageView imageView){
        Picasso.with(mContext).load(url).resize(width, height).centerCrop().into(imageView);
    }
    //加载图片有默认图片
    public static void loadImageViewH(Context mContext,String url, int loadImg,int errorImg,ImageView imageView){
        Picasso.with(mContext).load(url).placeholder(loadImg).error(errorImg).into(imageView);
    }

    //裁剪图片
    public static void loadImageViewCrop(Context mContext,String url, ImageView imageView) {
        Picasso.with(mContext).load(url).transform(new CropSquareTransformation()).into(imageView);
    }
    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() {
            return "square";
        }
    }


}

