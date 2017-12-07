package com.sendhand.xiyousecondhand.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.application.MyApplication;
import com.sendhand.xiyousecondhand.entry.Constants;
import com.sendhand.xiyousecondhand.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText etPhone;
    private EditText etPassword;
    private Dialog mCameraDialog; //底部菜单栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏手机状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPassword = (EditText) findViewById(R.id.etPassword);
        if (getIntent() != null) {
            Intent intent = getIntent();
            String phoneNumber = intent.getStringExtra("phoneNumber");
            String password = intent.getStringExtra("password");
            etPhone.setText(phoneNumber);
            etPassword.setText(password);
        }
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        TextView tvFindPassword = (TextView) findViewById(R.id.tvFindPassword);
        TextView tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(this);
        tvFindPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                String phoneNumber = etPhone.getText().toString();
                String password = etPassword.getText().toString();
                //判断输入
                if (phoneNumber.isEmpty() || phoneNumber.trim().equals("")) {
                    Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty() || password.equals("")) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (phoneNumber.contains(" ")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("登陆失败");
                    builder.setMessage("请输入正确的手机号。");
                    builder.show();
                }

                //向服务端发送数据进行验证
//                RequestBody requestBody = new FormBody.Builder()
//                        .add("phoneNumber", phoneNumber)
//                        .add("password", password)
//                        .build();
//                HttpUtil.postCallback(requestBody, Constants.URL + "/..**********", new okhttp3.Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        //异常情况
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        //接收服务端返回数据,并解析
//
//                        boolean isRight = true;
                        if (phoneNumber.equals("15829211215") && password.equals("111")) {
                            //数据验证正确,则跳转到主页面，并将用户信息发送
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            //发送数据
//                            intent.putExtra("", "");
                            startActivity(intent);
                            finish();
                        } else {
                            //数据有误,提示用户，重新输入,并将密码清空
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("登陆失败");
                            builder.setMessage("账号或密码错误，请重新输入。");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.show();
                        }
//                    }
//                });
                break;
            case R.id.tvRegister:
                requestPermissions();
                break;
            case R.id.tvFindPassword:
                //弹出底部菜单栏
                setDialog();
                break;
            case R.id.btnFindPassword:
                //找回密码
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //验证成功
                            @SuppressWarnings("unchecked")
                            HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                            final String phoneNumber = (String) phoneMap.get("phone");
                            //手机号提交到服务端，验证是否存在
//                            RequestBody requestBody = new FormBody.Builder()
//                            .add("phoneNumber", phoneNumber)
//                            .build();
//                            HttpUtil.postCallback(requestBody, Constants.URL + "/..**********", new okhttp3.Callback() {
//                                @Override
//                                public void onFailure(Call call, IOException e) {
//                                    //异常情况
//                                }
//                                @Override
//                                 public void onResponse(Call call, Response response) throws IOException {
//                                    //接收服务端返回数据,并解析
//                                    //手机号正确
                                    boolean isRight = true;
                                    if (isRight) {
                                        //跳转到修改密码界面
                                        Intent intent = new Intent(LoginActivity.this, ModifyPwdActivity.class);
                                        intent.putExtra("phoneNumber", phoneNumber);
                                        startActivity(intent);
                                    } else {
                                        //手机号不存在，提示用户
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getContext());
                                        builder.setTitle("错误");
                                        builder.setMessage("手机号不存在。");
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        builder.show();
                                    }
//                                }
//                            });
                        } else if (result == SMSSDK.RESULT_ERROR) {

                        }
                    }
                });
                registerPage.show(this);

                break;
            case R.id.btnSmsLogin:
                //短信验证登录


                break;
            case R.id.btnCancel:
                mCameraDialog.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 弹出底部菜单栏
     */
    private void setDialog() {
        mCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.activity_bottom_menu, null);
        //初始化视图
        root.findViewById(R.id.btnFindPassword).setOnClickListener(this);
        root.findViewById(R.id.btnSmsLogin).setOnClickListener(this);
        root.findViewById(R.id.btnCancel).setOnClickListener(this);
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    /**
     * 申请权限
     */
    private void requestPermissions() {
        //运行时权限申请
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_CONTACTS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECEIVE_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            registerPage();
        }
    }

    /**
     * 处理权限申请结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须开启所有权限", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    registerPage();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 转到短信验证界面
     */
    private void registerPage() {
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String phoneNumber = (String) phoneMap.get("phone");
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    registerIntent.putExtra("phoneNumber", phoneNumber);
                    startActivity(registerIntent);
                    // 提交用户信息
                    //registerUser(country, phone);
                } else if (result == SMSSDK.RESULT_ERROR) {

                }
            }
        });
        registerPage.show(this);
    }


}
