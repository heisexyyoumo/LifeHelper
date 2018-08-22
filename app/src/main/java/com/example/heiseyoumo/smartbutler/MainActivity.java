package com.example.heiseyoumo.smartbutler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heiseyoumo.smartbutler.entity.MyUser;
import com.example.heiseyoumo.smartbutler.fragment.ButlerFragment;
import com.example.heiseyoumo.smartbutler.fragment.GirlFragment;
import com.example.heiseyoumo.smartbutler.fragment.LiveFragment;
import com.example.heiseyoumo.smartbutler.fragment.PictureFragment;
import com.example.heiseyoumo.smartbutler.fragment.UserFragment;
import com.example.heiseyoumo.smartbutler.fragment.WeChatFragment;
import com.example.heiseyoumo.smartbutler.ui.ChooseWeatherActivity;
import com.example.heiseyoumo.smartbutler.ui.LoginActivity;
import com.example.heiseyoumo.smartbutler.ui.SettingActivity;
import com.example.heiseyoumo.smartbutler.ui.UserInfoActivity;
import com.example.heiseyoumo.smartbutler.utils.L;
import com.example.heiseyoumo.smartbutler.utils.UtilTools;
import com.example.heiseyoumo.smartbutler.widgets.HeaderBar;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    //Tablayout
    private TabLayout mTabLayout;
    //Viewpager
    private ViewPager mViewPager;
    //Title
    private List<String> mTitle;
    //Fragment
    private List<Fragment> mFragment;
    //悬浮窗
    private FloatingActionButton fab_setting;
    //自定义HeadBar
    private HeaderBar mHeadBar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNv;

    private RelativeLayout mUserInfoRl;
    //HeaderLayout
    private TextView mDescTv;
    private TextView mNameTv;
    private CircleImageView mIconCv;

    private MyUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = BmobUser.getCurrentUser(MyUser.class);
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            initData();
            initView();
        }
    }


    //初始化view
    private void initView() {
        mHeadBar = (HeaderBar) findViewById(R.id.mHeadBar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawerLayout);
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_setting.setOnClickListener(this);
        //默认隐藏
        fab_setting.setVisibility(View.GONE);
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) findViewById(R.id.mViewpager);
        //NavigationView
        mNv = (NavigationView) findViewById(R.id.mNv);
        //得到NavigationView的头布局
        View headerLayout = mNv.getHeaderView(0);
        //初始化头布局中的元素
        mUserInfoRl = (RelativeLayout) headerLayout.findViewById(R.id.mUserInfoRl);
        mUserInfoRl.setOnClickListener(this);
        mDescTv = (TextView) headerLayout.findViewById(R.id.mDescTv);
        mNameTv = (TextView) headerLayout.findViewById(R.id.mNameTv);
        mIconCv = (CircleImageView) headerLayout.findViewById(R.id.mIconCv);
        //加载数据
        mNameTv.setText(user.getUsername());
        mDescTv.setText(user.getDesc());
        UtilTools.getImageToShare(this, mIconCv);
        //menu的监听事件
        mNv.setNavigationItemSelectedListener(this);


        //预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());

        //HeadBar的点击事件
        mHeadBar.setClickCallBack(new HeaderBar.ClickCallBack() {
            @Override
            public void onMenuClick() {
                //点击开启DrawerLayout
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //mViewPager的滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab_setting.setVisibility(View.GONE);

                } else {
                    fab_setting.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回item个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        //绑定
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //初始化数据
    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add(getString(R.string.nSmart));
        mTitle.add(getString(R.string.nWeChat));
        mTitle.add(getString(R.string.nGirl));
        mTitle.add(getString(R.string.nLive));
        //mTitle.add(getString(R.string.nUser));

        mFragment = new ArrayList<>();
        mFragment.add(new ButlerFragment());
        mFragment.add(new WeChatFragment());
        //mFragment.add(new GirlFragment());
        mFragment.add(new PictureFragment());
        //mFragment.add(new UserFragment());
        mFragment.add(new LiveFragment());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.mUserInfoRl:
                startActivity(new Intent(this, UserInfoActivity.class));
                finish();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mSettingNav:
                startActivity(new Intent(MainActivity.this,
                        SettingActivity.class));
                break;
            case R.id.mWeatherNav:
                startActivity(new Intent(MainActivity.this,
                        ChooseWeatherActivity.class));
                break;
        }
        return true;
    }
}
