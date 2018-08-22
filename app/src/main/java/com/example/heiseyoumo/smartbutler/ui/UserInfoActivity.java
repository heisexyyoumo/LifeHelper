package com.example.heiseyoumo.smartbutler.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.example.heiseyoumo.smartbutler.MainActivity;
import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.View.CustomDialog;
import com.example.heiseyoumo.smartbutler.entity.MyUser;
import com.example.heiseyoumo.smartbutler.utils.L;
import com.example.heiseyoumo.smartbutler.utils.UtilTools;
import com.example.heiseyoumo.smartbutler.widgets.HeadToolBar;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int PICTURE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private File tempFile = null;

    //标题栏自定义buju
    private HeadToolBar mHeadToolBar;
    private EditText mNameEt;
    private RadioButton mGenderMaleRb;
    private RadioButton mGenderFemaleRb;
    private EditText mSignEt;
    //退出登录按钮
    private Button mLogoutBtn;
    //头像的layout
    private RelativeLayout mIconLayout;
    //圆形头像
    private CircleImageView mIconCv;
    //Bmob的实体类
    private MyUser user;
    //自定义dialog
    private CustomDialog dialog;
    //dialog的按钮
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        loadData();
        listenerEvent();
    }

    //初始化布局
    private void initView() {
        //标题栏
        mHeadToolBar = (HeadToolBar) findViewById(R.id.mHeadToolBar);
        //个人信息
        mNameEt = (EditText) findViewById(R.id.mNameEt);
        mSignEt = (EditText) findViewById(R.id.mSignEt);
        mGenderMaleRb = (RadioButton) findViewById(R.id.mGenderMaleRb);
        mGenderFemaleRb = (RadioButton) findViewById(R.id.mGenderFemaleRb);
        //退出登录
        mLogoutBtn = (Button) findViewById(R.id.mLogoutBtn);
        //头像相关
        mIconLayout = (RelativeLayout) findViewById(R.id.mIconLayout);
        mIconCv = (CircleImageView) findViewById(R.id.mIconCv);
//        // /初始化dialog
//        dialog = new CustomDialog(this, 0, 0,
//                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
//        //屏幕外点击无效
//        dialog.setCancelable(false);
//        //初始化按钮
//        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
//        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
//        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

    }

    //加载数据
    private void loadData() {
        user = BmobUser.getCurrentUser(MyUser.class);
        mNameEt.setText(user.getUsername());
        mSignEt.setText(user.getDesc());
        if (user.getSex()) {
            mGenderMaleRb.setChecked(true);
        } else {
            mGenderFemaleRb.setChecked(true);
        }
        //加载图片
        UtilTools.getImageToShare(this, mIconCv);
    }

    //监听事件
    private void listenerEvent() {
        //HeadToolBar的点击监听事件
        mHeadToolBar.setClickCallBack(new HeadToolBar.ClickCallBack() {
            @Override
            public void onBackClick() {
                startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onSaveClick() {
                String userName = mNameEt.getText().toString().trim();
                String userDesc = mSignEt.getText().toString().trim();
                Boolean sex = mGenderMaleRb.isChecked();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userDesc)) {
                    //更新属性
                    user.setUsername(userName);
                    user.setDesc(userDesc);
                    user.setSex(sex);
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(UserInfoActivity.this,
                                        "更新信息成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserInfoActivity.this,
                                        "更新信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
                finish();
            }
        });

        //退出登录
        mLogoutBtn.setOnClickListener(this);

        //头像操作
        mIconLayout.setOnClickListener(this);
//        //拍照
//        btn_camera.setOnClickListener(this);
//        //从相册选取
//        btn_picture.setOnClickListener(this);
//        //取消
//        btn_cancel.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //退出登录
            case R.id.mLogoutBtn:
                MyUser.logOut();
                // 现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(UserInfoActivity.this,
                        LoginActivity.class));
                UserInfoActivity.this.finish();
                break;
            //显示dialog
            case R.id.mIconLayout:
                new AlertView("上传头像", null, "取消", null,
                        new String[]{"拍照", "从相册中选择"},
                        this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                toCamera();
                                break;
                            case 1:
                                toPicture();
                                break;
                        }
                    }
                }).show();
                break;
//            //拍照
//            case R.id.btn_camera:
//                toCamera();
//                break;
//            //从相册选取
//            case R.id.btn_picture:
//                toPicture();
//                break;
//            //取消
//            case R.id.btn_cancel:
//                dialog.dismiss();
//                break;
        }

    }

    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用,科通就进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                        PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        //dialog.dismiss();
    }

    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_REQUEST_CODE);
        //dialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case PICTURE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //裁剪图片
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            mIconCv.setImageBitmap(bitmap);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UtilTools.putImageToShare(this, mIconCv);
    }
}
