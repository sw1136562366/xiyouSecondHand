package com.sendhand.xiyousecondhand.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.SharedPrefercesUtil;
import com.sendhand.xiyousecondhand.util.ToastUtil;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.MainActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.sendhand.xiyousecondhand.util.SharedPrefercesUtil.clearData;
import static com.sendhand.xiyousecondhand.util.SharedPrefercesUtil.saveObject;
import static com.sendhand.xiyousecondhand.view.LoginActivity.loginActivitySign;

/**
 * Mob 短信验证 测试demo
 */
public class SmsSendActivity extends BaseActivity implements View.OnClickListener {
    private EditText etPhone;
    private Button btnSms;
    private EditText etCode;
    private TextView tvGetCode;
    private ProgressBar ladingProgressBar;
    private TextView ladingSign;
    private TextView progressbarBack;
    private boolean tvGetCodesClicked = false;
    //判断哪个活动的标志
    private String sign;
    //验证手机号是否存在（volatile子线程同步变量）
    private volatile Boolean isNotRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏手机状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sms_send);
        sign = getIntent().getStringExtra("sign");
        initSMSSDK();
        initView();

    }

    private void initView() {
        ladingProgressBar = (ProgressBar) findViewById(R.id.loading_progressBar);
        ladingSign = (TextView) findViewById(R.id.lading_sign);
        progressbarBack = (TextView) findViewById(R.id.progress_background);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCode = (EditText) findViewById(R.id.et_code);
        tvGetCode = (TextView) findViewById(R.id.get_code);
        btnSms = (Button) findViewById(R.id.btnSms);
        setUnUseBtn();
        //按钮不可用
        btnSms.setEnabled(false);
        btnSms.setBackgroundColor(Color.GRAY);

        tvGetCode.setEnabled(false);
        tvGetCode.setBackgroundColor(Color.GRAY);

        tvGetCode.setOnClickListener(this);
        btnSms.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code:
                tvGetCode.requestFocus();
                if (phoneIsRight()) {
                    tvGetCodesClicked = true;
                    tvGetCode.setEnabled(false);
                    tvGetCode.setBackgroundColor(Color.GRAY);
                    //启动获取验证码 86是中国
                    String phone = etPhone.getText().toString().trim();
                    SMSSDK.getVerificationCode("86", phone);//发送短信验证码到手机号
                    timer.start();//使用计时器 设置验证码的时间限制
                }
                break;
            case R.id.btnSms:
                submitInfo();
                break;
            default:
                break;
        }
    }

    /**
     * 检测手机号是否存在
     * @return
     */
    private boolean phoneIsRight() {
        final boolean[] bool = new boolean[1];
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("tel", etPhone.getText().toString());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (sign.equals("register")) {
                        if (object.size() == 0) {
                            //手机号没注册，继续进行注册
                            isNotRegister = true;
                        } else {
                            isNotRegister = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast(SmsSendActivity.this, "该手机号已注册");
                                }
                            });
                        }
                    } else if (sign.equals("modifyPassword") || sign.equals("smsLogin")) {
                        if (object.size() > 0) {
                            //手机号已注册，可以进行修改和登录
                            isNotRegister = true;
                        } else {
                            isNotRegister = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast(SmsSendActivity.this, "该手机号未注册");
                                }
                            });
                        }
                    }
                } else {
                    LogUtil.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


//        RequestBody requestBody = new FormBody.Builder()
//            .add("phoneNumber", etPhone.getText().toString())
//            .build();
//            HttpUtil.postCallback(requestBody, Constants.VALIDATE_PHONE_URL, new okhttp3.Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    LogUtil.d("SmsSendActivity", e.getMessage());
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    String getReturn = response.body().string();
//                    LogUtil.d("SmsSendActivity", "return:" + getReturn);
//                    if (sign.equals("register")) {
//                        if (getReturn.equals("1")) {
//                            //手机号没注册，继续进行注册
//                            isNotRegister = true;
//                        } else {
//                            isNotRegister = false;
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ToastUtil.showToast(SmsSendActivity.this, "该手机号已注册");
//                                }
//                            });
//                        }
//                    } else if (sign.equals("modifyPassword") || sign.equals("smsLogin")) {
//                        if (getReturn.equals("0")) {
//                            //手机号已注册，可以进行修改和登录
//                            isNotRegister = true;
//                        } else {
//                            isNotRegister = false;
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ToastUtil.showToast(SmsSendActivity.this, "该手机号未注册");
//                                }
//                            });
//                        }
//                    }
//
//                }
//            });
        return isNotRegister;
    }

    private void initSMSSDK() {
        //初始化短信验证
//        SMSSDK.initSDK(this, APPKEY, APPSECRECT);

        //注册短信回调
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                switch (event) {
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            Message msg = new Message();
                            msg.what = 0;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                        break;
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            boolean smart = (Boolean) data;
                            if (smart) {
                                Message message = new Message();//智能验证成功
                                message.what = 2;
                                handler.sendMessage(message);
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        //  startActivity(new Intent(WriteVerificationCode.this,ModifyPassword.class));
                                        //WriteVerificationCode.this.finish();
                                    }
                                }, 2000);
                            }
                    }
                }
            }
        });
    }
    /**
     * 需要开启一个主线程来显示提示
     */

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int code1 = msg.what;
            switch (code1) {
                case 0:
                    ToastUtil.showToast(SmsSendActivity.this, "验证成功");
                    //执行验证成功的操作
                    if (sign.equals("register")) {
                        //注册
                        Intent registerIntent = new Intent(SmsSendActivity.this, RegisterActivity.class);
                        registerIntent.putExtra("phoneNumber", etPhone.getText().toString());
                        startActivity(registerIntent);
                        finish();
                    } else if (sign.equals("modifyPassword")) {
                        //跳转到修改密码界面
                        Intent modifyPwdIntent = new Intent(SmsSendActivity.this, ModifyPwdActivity.class);
                        modifyPwdIntent.putExtra("phoneNumber", etPhone.getText().toString());
                        startActivity(modifyPwdIntent);
                        finish();
                    } else if (sign.equals("smsLogin")) {
                        //登录提示
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ladingProgressBar.setVisibility(View.VISIBLE);
                                ladingSign.setVisibility(View.VISIBLE);
                                progressbarBack.setVisibility(View.VISIBLE);
                            }
                        });
                        //验证登录
                        BmobQuery<User> query = new BmobQuery<User>();
                        query.addWhereEqualTo("tel", etPhone.getText().toString());
                        query.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> object, BmobException e) {
                                if (e == null) {
                                    if (object.size() > 0) {
                                        //登录成功
                                        User user = object.get(0);
//                            user.setPic("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_ca79a146.png");
                                        //登录成功，从融云服务器获取token
                                        if (user.getTel() != null && SharedPrefercesUtil.getTokenFromSP(MyApplication.getContext(), user.getTel()) == null) {
                                            LoginActivity.getRYToken(user);
                                        }

                                        Intent intent = new Intent(SmsSendActivity.this, MainActivity.class);
                                        //发送数据user
                                        intent.putExtra("user_data", user);
                                        startActivity(intent);
                                        //先清空，保存用户信息sharedPreferences
                                        clearData(MyApplication.getContext());
                                        saveObject(MyApplication.getContext(), user);
                                        finish();
                                    }
                                } else {
                                    LogUtil.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });

                        //再次发送http请求，获取用户信息
//                        RequestBody requestBody = new FormBody.Builder()
//                                .add("phoneNumber", etPhone.getText().toString())
//                                .build();
//                        HttpUtil.postCallback(requestBody, Constants.VALIDATE_LOGIN_URL, new okhttp3.Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                LogUtil.d("SmsSendActivity", e.getMessage());
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//                                byte[] bytes = response.body().bytes();
//                                String jsonUser = new String(bytes, "GBK");
//                                LogUtil.d("SmsSendActivity", jsonUser);
//                                User user = GsonUtil.parseJsonWithGson(jsonUser);
////                                Intent intent = new Intent(SmsSendActivity.this, MainActivity.class);
//                                //发送数据user
////                                intent.putExtra("user_data", user);
////                                startActivity(intent);
//                            }
//                        });
                        //关闭登录界面
                        loginActivitySign.finish();
                        finish();
                    }
                    break;
                case 1:
                    ToastUtil.showToast(SmsSendActivity.this, "您提交的验证码有误");
                    break;
                case 2:
                    etCode.setText("该号码为可信任号码");
                    ToastUtil.showToast(SmsSendActivity.this, "智能验证成功，即将为您跳转页面");
                    break;
                case 3:
                    ToastUtil.showToast(SmsSendActivity.this, "验证码获取失败");
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 验证用户的其他信息
     * 这里验证两次密码是否一致 以及验证码判断
     */
    private void submitInfo() {
        //密码验证
//        KLog.e("提交按钮点击了");
        String phone = etPhone.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        SMSSDK.submitVerificationCode("86", phone, code);//提交验证码  在eventHandler里面查看验证结果
    }

    /**
     * 使用计时器来限定验证码
     * 在发送验证码的过程 不可以再次申请获取验证码 在指定时间之后没有获取到验证码才能重新进行发送
     * 这里限定的时间是60s
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setText((millisUntilFinished / 1000) + "秒后重发");
        }

        @Override
        public void onFinish() {
            tvGetCode.setEnabled(true);
            tvGetCode.setBackgroundResource(R.drawable.login_button_back);
            tvGetCode.setText("获取验证码");
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止使用短信验证 产生内存溢出问题
        SMSSDK.unregisterAllEventHandler();
    }

    /**
     * 输入框监听器
     */
    private void setUnUseBtn() {
        //EditText监听器
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnSms.setEnabled(false);
                btnSms.setBackgroundColor(Color.GRAY);
                tvGetCode.setEnabled(false);
                tvGetCode.setBackgroundColor(Color.GRAY);
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPhone.getText().length() == 11 && isMobileNO(etPhone.getText().toString()) && etCode.getText().length() == 4) {
                    btnSms.setEnabled(true);
                    btnSms.setBackgroundResource(R.drawable.login_button_back);
                }

                //如果没有点击过获取验证码
                if (!tvGetCodesClicked) {
                    if (etPhone.getText().length() == 11) {
                        tvGetCode.setEnabled(true);
                        tvGetCode.setBackgroundResource(R.drawable.login_button_back);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        etPhone.addTextChangedListener(textWatcher);
        etCode.addTextChangedListener(textWatcher);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return mobiles.matches(telRegex);
    }
}
