package com.sendhand.xiyousecondhand.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.ToastUtil;

public class ModifyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUserInfo;
    private String what_info;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        what_info = getIntent().getStringExtra("what_info");
        user = getIntent().getParcelableExtra("user_data");
        initView();

    }

    private void initView() {
        etUserInfo = (EditText) findViewById(R.id.etUserInfo);
        Button btnSaveUserInfo = (Button) findViewById(R.id.btnSaveUserInfo);
        btnSaveUserInfo.setOnClickListener(this);
        //返回按钮
        TextView btnBack = (TextView) findViewById(R.id.btnTopTitleLeft);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        //标题
        TextView tvTopTitleCenter = (TextView) findViewById(R.id.tvTopTitleCenter);
        if (what_info.equals("userName")) {
            tvTopTitleCenter.setText("昵称");
            String userName = user.getUsername();
            etUserInfo.setText(userName);
            etUserInfo.setSelection(userName.length());//将光标移至文字末尾
        } else if (what_info.equals("age")) {
            tvTopTitleCenter.setText("年龄");
            etUserInfo.setText(user.getAge() + "");
            etUserInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
            etUserInfo.setSelection((user.getAge() + "").length());//将光标移至文字末尾
        } else if (what_info.equals("school")) {
            tvTopTitleCenter.setText("院校名称");
            etUserInfo.setText(user.getSchool());
            etUserInfo.setSelection(user.getSchool().length());//将光标移至文字末尾
        } else if (what_info.equals("email")) {
            tvTopTitleCenter.setText("邮箱");
            etUserInfo.setText(user.getEmail());
            etUserInfo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            etUserInfo.setSelection(user.getEmail().length());//将光标移至文字末尾
        } else if (what_info.equals("address")) {
            tvTopTitleCenter.setText("收货地址");
            etUserInfo.setText(user.getAddress());
            etUserInfo.setSelection(user.getAddress().length());//将光标移至文字末尾
        } else if (what_info.equals("postwd")) {
            tvTopTitleCenter.setText("邮编");
            etUserInfo.setText(user.getPostwd());
            etUserInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
            etUserInfo.setSelection(user.getPostwd().length());//将光标移至文字末尾
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTopTitleLeft:
                finish();
                break;
            case R.id.btnSaveUserInfo:
                if (what_info.equals("userName")) {
                    if (etUserInfo.getText().length() <= 10 && !etUserInfo.getText().toString().contains(" ")) {
                        if (!etUserInfo.getText().equals(user.getUsername())) {
                            user.setUsername(etUserInfo.getText().toString());
                            //数据库修改昵称

                        }
                    } else {
                        ToastUtil.showToast(this, "输入不合法");
                    }
                } else if (what_info.equals("age")) {
                    if (etUserInfo.getText().length() <= 3) {
                        if (!etUserInfo.getText().equals(user.getAge())) {
                            user.setAge(Integer.parseInt(etUserInfo.getText().toString()));
                            //数据库修改

                        }

                    } else {
                        ToastUtil.showToast(this, "输入不合法");
                    }
                } else if (what_info.equals("school")) {
                    if (!etUserInfo.getText().toString().contains(" ")) {
                        if (!etUserInfo.getText().equals(user.getSchool())) {
                            user.setSchool(etUserInfo.getText().toString());
                            //数据库修改

                        }
                    } else {
                        ToastUtil.showToast(this, "输入不合法");
                    }
                } else if (what_info.equals("email")) {
                    if (!etUserInfo.getText().equals(user.getEmail())) {
                        user.setEmail(etUserInfo.getText().toString());
                        //数据库修改

                    }
                } else if (what_info.equals("address")) {
                    if (!etUserInfo.getText().equals(user.getAddress())) {
                        user.setAddress(etUserInfo.getText().toString());
                        //数据库修改

                    }
                } else if (what_info.equals("postwd")) {
                    if (!etUserInfo.getText().equals(user.getPostwd())) {
                        user.setPostwd(etUserInfo.getText().toString());
                        //数据库修改

                    }
                }

                ToastUtil.showToast(this, "保存成功");
                Intent intent = new Intent(this, PersonInfoActivity.class);
                intent.putExtra("user_data", user);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
