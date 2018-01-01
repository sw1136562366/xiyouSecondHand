package com.sendhand.xiyousecondhand.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.entry.User;

import java.io.File;
import java.io.IOException;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener{

    private User user;
    private TextView tvSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        user = getIntent().getParcelableExtra("user_data");
        initView();

    }

    private void initView() {
        //返回按钮
        TextView btnBack = (TextView) findViewById(R.id.btnTopTitleLeft);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        //标题
        TextView tvTopTitleCenter = (TextView) findViewById(R.id.tvTopTitleCenter);
        tvTopTitleCenter.setText("我的资料");
        //显示用户信息
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvPostwd = (TextView) findViewById(R.id.tvPostwd);
        TextView tvAge = (TextView) findViewById(R.id.tvAge);
        TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvSex = (TextView) findViewById(R.id.tvSex);
        TextView tvSchool = (TextView) findViewById(R.id.tvSchool);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvUserName.setText(user.getUsername());
        tvAge.setText(user.getAge() + "");
        tvPostwd.setText(user.getPostwd());
        tvSex.setText(user.getSex());
        if (user.getSchool().length() > 10) {
            tvSchool.setText(user.getSchool().substring(0, 10) + "...");
        } else {
            tvSchool.setText(user.getSchool());
        }

        tvEmail.setText(user.getEmail());
        if (user.getAddress().length() > 10) {
            tvAddress.setText(user.getAddress().substring(0, 10) + "...");
        } else {
            tvAddress.setText(user.getAddress());
        }

        RelativeLayout rlUserName = (RelativeLayout) findViewById(R.id.rlUserName);
        rlUserName.setOnClickListener(this);
        RelativeLayout rlSex = (RelativeLayout) findViewById(R.id.rlSex);
        rlSex.setOnClickListener(this);
        RelativeLayout rlAge = (RelativeLayout) findViewById(R.id.rlAge);
        rlAge.setOnClickListener(this);
        RelativeLayout rlSchool = (RelativeLayout) findViewById(R.id.rlSchool);
        rlSchool.setOnClickListener(this);
        RelativeLayout rlEmail = (RelativeLayout) findViewById(R.id.rlEmail);
        rlEmail.setOnClickListener(this);
        RelativeLayout rlAdderes = (RelativeLayout) findViewById(R.id.rlAdderes);
        rlAdderes.setOnClickListener(this);
        RelativeLayout rlPostwd = (RelativeLayout) findViewById(R.id.rlPostwd);
        rlPostwd.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTopTitleLeft:
                finish();
                break;
            case R.id.rlUserName:
                Intent userNameIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                userNameIntent.putExtra("what_info", "userName");
                userNameIntent.putExtra("user_data", user);
                startActivity(userNameIntent);
                break;
            case R.id.rlSex:
                //对话框
                showChoosePicDialog();
                break;
            case R.id.rlAge:
                Intent ageIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                ageIntent.putExtra("what_info", "age");
                ageIntent.putExtra("user_data", user);
                startActivity(ageIntent);
                break;
            case R.id.rlSchool:
                Intent schoolIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                schoolIntent.putExtra("what_info", "school");
                schoolIntent.putExtra("user_data", user);
                startActivity(schoolIntent);
                break;
            case R.id.rlEmail:
                Intent emailIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                emailIntent.putExtra("what_info", "email");
                emailIntent.putExtra("user_data", user);
                startActivity(emailIntent);
                break;
            case R.id.rlAdderes:
                Intent addressIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                addressIntent.putExtra("what_info", "address");
                addressIntent.putExtra("user_data", user);
                startActivity(addressIntent);
                break;
            case R.id.rlPostwd:
                Intent postwdIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                postwdIntent.putExtra("what_info", "postwd");
                postwdIntent.putExtra("user_data", user);
                startActivity(postwdIntent);
                break;
            default:
                break;
        }
    }

    /**
     * 显示修改性别对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改性别");
        String[] items = { "男", "女" };
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //男
                        if (!tvSex.getText().equals("男")) {
                            tvSex.setText("男");
                            //保存数据库，网络请求


                        }
                        break;
                    case 1: // 女
                        if (!tvSex.getText().equals("女")) {
                            tvSex.setText("女");


                        }
                        break;
                }
            }
        });
        builder.create().show();
    }
}
