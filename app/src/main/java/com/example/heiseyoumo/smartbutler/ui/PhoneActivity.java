package com.example.heiseyoumo.smartbutler.ui;
/**
 * 归属地查询
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heiseyoumo.smartbutler.R;
import com.example.heiseyoumo.smartbutler.utils.L;
import com.example.heiseyoumo.smartbutler.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneActivity extends BaseActivity implements View.OnClickListener {

    //输入框
    private EditText et_number;
    //公司logo
    private ImageView iv_company;
    //结果
    private TextView tv_result;
    //Button
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_0;
    private Button btn_del;
    private Button btn_enter;

    //标记位
    private Boolean flag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        initView();
    }

    //初始化View
    private void initView() {
        et_number = (EditText)findViewById(R.id.et_number);
        iv_company = (ImageView) findViewById(R.id.iv_company);
        tv_result = (TextView)findViewById(R.id.tv_result);
        btn_1 = (Button)findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = (Button)findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = (Button)findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = (Button)findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = (Button)findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = (Button)findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = (Button)findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = (Button)findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = (Button)findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_0 = (Button)findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_del = (Button)findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        btn_enter = (Button)findViewById(R.id.btn_enter);
        btn_enter.setOnClickListener(this);
        
        //长按事件
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_number.setText("");
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        /**
         * 逻辑
         * 1.获取输入框的内容
         * 2.判断是否为空
         * 3.网络请求
         * 4.解析Json
         * 5.结果显示
         *
         * ---------
         * 键盘逻辑
         */

        //1.获取输入框的内容
        String str = et_number.getText().toString().trim();
        switch (v.getId()){
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
            case R.id.btn_0:
                if(flag){
                    str = "";
                    et_number.setText("");
                    flag =false;
                }
                //每次结尾增加一个数字
                et_number.setText(str + ((Button)v).getText());
                //移动光标
                et_number.setSelection(str.length() + 1);
                break;
            case R.id.btn_del:
                if(!TextUtils.isEmpty(str) & str.length() > 0){
                    //每次结尾减去一个数字
                    et_number.setText(str.substring(0,str.length() - 1));
                    //移动光标
                    et_number.setSelection(str.length() - 1);
                }
                break;
            case R.id.btn_enter:
                if(!TextUtils.isEmpty(str)){
                    getPhone(str);
                }
                break;
        }

    }

    private void getPhone(String str) {
        //拼接url
        String url = "http://apis.juhe.cn/mobile/get?phone="+ str + "&key=" + StaticClass.PHONE_KEY;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(PhoneActivity.this,"结果：" + t,Toast.LENGTH_SHORT).show();
                L.i("phone:"+ t );
                parsingJson(t);
            }
        });
    }

    /**
     * "province":"四川",
     "city":"遂宁",
     "areacode":"0825",
     "zip":"629000",
     "company":"移动",
     "card":""
     *
     */
    //解析Json
    private void parsingJson(String t) {

        try {
            JSONObject jsonObject = jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            String province = jsonResult.getString("province");
            String city = jsonResult.getString("city");
            String areacode = jsonResult.getString("areacode");
            String zip = jsonResult.getString("zip");
            String company = jsonResult.getString("company");
           // String card = jsonResult.getString("card");

            tv_result.setText("归属地：" + province + city + "\n" + "区号：" + areacode + "\n"
            + "邮编：" + zip + "\n" + "运营商：" + company + "\n");
            //tv_result.append("区号：" + areacode + "\n");
            //tv_result.append("邮编：" + zip + "\n");
            //tv_result.append("运营商：" + company + "\n");
            //tv_result.append("类型：" + card + "\n");

            //图片显示
            switch (company){
                case "移动":
                    iv_company.setBackgroundResource(R.drawable.china_mobile);
                    break;
                case "联通":
                    iv_company.setBackgroundResource(R.drawable.china_unicom);
                    break;
                case "电信":
                    iv_company.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }
            flag = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
