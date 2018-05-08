package com.sendhand.xiyousecondhand.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.entry.Constants;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.HttpUtil;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.MD5Util;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sendhand.xiyousecondhand.R.drawable.login_button_back;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private EditText etUserName;
    private EditText etPassword;
    private Button btnRegister;
    private ProgressBar ladingProgressBar;
    private TextView ladingSign;
    private TextView progressbarBack;
    private static String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        ladingProgressBar = (ProgressBar) findViewById(R.id.loading_progressBar);
        ladingSign = (TextView) findViewById(R.id.lading_sign);
        progressbarBack = (TextView) findViewById(R.id.progress_background);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        //设置按钮不可用
        btnRegister.setEnabled(false);
        btnRegister.setBackgroundColor(Color.GRAY);
        setUnUseBtn();
        btnRegister.setOnClickListener(this);
    }

    /**
     * 输入框监听器
     */
    private void setUnUseBtn() {
        //EditText监听器
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnRegister.setEnabled(false);
                btnRegister.setBackgroundColor(Color.GRAY);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(etUserName.getText()) && etPassword.getText().length() >= 6) {
                    btnRegister.setEnabled(true);
                    btnRegister.setBackgroundResource(R.drawable.login_button_back);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                setEditTextInhibitInputSpace(etPassword);
                setEditTextInhibitInputSpeChat(etPassword);
            }
        };
        etUserName.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                ladingProgressBar.setVisibility(View.VISIBLE);
                ladingSign.setVisibility(View.VISIBLE);
                progressbarBack.setVisibility(View.VISIBLE);

                final String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                LogUtil.d("RegisterActivity", phoneNumber);
                LogUtil.d("RegisterActivity", userName);
                LogUtil.d("RegisterActivity", password);
                //密码加密
                password = MD5Util.md5(password);

                //bmob插入数据
                //注意：不能调用gameScore.setObjectId("")方法
                User user = new User();
                user.setTel(phoneNumber);
                user.setUsername(userName);
                user.setPassword(password);
                user.save(new SaveListener<String>() {

                    @Override
                    public void done(String objectId, BmobException e) {
                        if(e==null){
                            //注册成功，跳转到登录
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.putExtra("phoneNumber", phoneNumber);
                            startActivity(intent);
                            finish();
                        }else{
                            LogUtil.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //打印错误信息
                                    ladingProgressBar.setVisibility(View.GONE);
                                    ladingSign.setVisibility(View.GONE);
                                    progressbarBack.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });

                //验证手机号
//                    //向服务端发送数据
//                RequestBody requestBody = new FormBody.Builder()
//                        .add("phoneNumber", phoneNumber)
//                        .add("userName", userName)
//                        .add("password", password)
//                        .build();
//                HttpUtil.postCallback(requestBody, Constants.REGISTER_URL, new okhttp3.Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        //异常情况
//                        LogUtil.d("RegisterActivity", e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if (response.isSuccessful()) {
//                            String returnGet = response.body().string();
//                            LogUtil.d("RegisterActivity", "return:" + returnGet);
//                            if (returnGet.equals("1") || returnGet == "1") {
//                                //注册成功，跳转到登录
//                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                intent.putExtra("phoneNumber", phoneNumber);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        //打印错误信息
//                                        ladingProgressBar.setVisibility(View.GONE);
//                                        ladingSign.setVisibility(View.GONE);
//                                        progressbarBack.setVisibility(View.GONE);
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });
                break;
            default:
                break;
        }
    }

    /**
     * 验证手机号是否已经注册
     * @return
     */
    public static boolean validatePhone() {
        final boolean[] bool = new boolean[1];
//        //向服务端发送数据
//        RequestBody requestBody = new FormBody.Builder()
//                .add("phoneNumber", phoneNumber)
//                .build();
//        HttpUtil.postCallback(requestBody, Constants.VALIDATE_PHONE_URL, new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //异常情况
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String returnGet = response.body().toString();
//                if (returnGet.equals("1")) {
//                   //正确
//                    bool[0] = true;
//                } else {
//                    bool[0] = false;
//                }
//            }
//        });
        return bool[0];
    }

    /**
     * 禁止EditText输入空格
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.toString().equals(" ")) {
                    return "";
                }

                if (TextUtils.equals(source, "  ")) {
                    return "";
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText){

        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(16)});
    }
}
