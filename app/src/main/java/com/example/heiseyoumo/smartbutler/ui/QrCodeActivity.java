package com.example.heiseyoumo.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.heiseyoumo.smartbutler.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * 生成二维码
 */
public class QrCodeActivity extends BaseActivity {

    //我的二维码
    private ImageView iv_qr_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        
        initView();
    }

    private void initView() {
        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        Bitmap  mBitmap = CodeUtils.createImage("你好", 400, 400, null);
        iv_qr_code.setImageBitmap(mBitmap);
    }
}
