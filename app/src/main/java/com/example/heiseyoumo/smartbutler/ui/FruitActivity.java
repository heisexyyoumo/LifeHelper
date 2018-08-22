package com.example.heiseyoumo.smartbutler.ui;

/*
    水果详情界面
 */

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.heiseyoumo.smartbutler.R;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class FruitActivity extends AppCompatActivity {

    public static final String FRUIT_NAME = "fruit_name";
    public static final String FRUIT_IMAGE_ID = "fruit_image_id";

    private android.support.v7.widget.Toolbar toolbar;
    private CollapsingToolbarLayout collToolBar;
    private ImageView iv_img;
    private TextView tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);
        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        String fruitName = intent.getStringExtra(FRUIT_NAME);
        int fruitImageId = intent.getIntExtra(FRUIT_IMAGE_ID, 0);
        collToolBar = (CollapsingToolbarLayout) findViewById(R.id.collToolBar);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        tv_content = (TextView) findViewById(R.id.tv_content);
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collToolBar.setTitle(fruitName);
        Picasso.with(this).load(fruitImageId).into(iv_img);
        String fruitContent = generateFruitContent(fruitName);
        tv_content.setText(fruitContent);
    }

    private String generateFruitContent(String fruitName) {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        int num = random.nextInt(5);
        //循环次数为100-500
        int cycleTimes = (num + 1) * 100;
        for (int i = 0; i < cycleTimes; i++) {
            builder.append(fruitName);
        }
        return builder.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
