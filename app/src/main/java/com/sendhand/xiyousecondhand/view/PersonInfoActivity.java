package com.sendhand.xiyousecondhand.view;

import android.app.Activity;
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
import com.sendhand.xiyousecondhand.entry.Constants;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.HttpUtil;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sendhand.xiyousecondhand.util.SharedPrefercesUtil.clearData;
import static com.sendhand.xiyousecondhand.util.SharedPrefercesUtil.saveObject;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener{

    private User user;
    private TextView tvSex;
    private TextView tvUserName;
    private TextView tvPostwd;
    private TextView tvAge;
    private TextView tvAddress;
    private  TextView tvSchool;
    private TextView tvEmail;
    public static PersonInfoActivity personInfoSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        personInfoSign = this;
        if (getIntent() != null) {
            user = (User) getIntent().getSerializableExtra("user_data");
        }
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

        if (user.getAge() != null) {
            tvAge.setText(user.getAge() + "");
        }

        tvPostwd.setText(user.getPostwd());
        if (user.getSex() != null) {
            tvSex.setText(user.getSex().equals("0") ? "男" : "女");
        }

        if (user.getSchool() != null && user.getSchool().length() > 10) {
            tvSchool.setText(user.getSchool().substring(0, 10) + "...");
        } else {
            tvSchool.setText(user.getSchool());
        }

        tvEmail.setText(user.getEmail());
        if (user.getAddress() != null && user.getAddress().length() > 10) {
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

    /**
     * user传递给上一个活动
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("user_data", user);
//        intent.putExtra("text", "测试啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
//        LogUtil.d("PersonInfoActivity", user.getSchool());
        setResult(RESULT_OK, intent);
        finish();
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
                startActivityForResult(userNameIntent, 2);
//                startActivity(userNameIntent);
                break;
            case R.id.rlSex:
                //对话框
                showChoosePicDialog();
                break;
            case R.id.rlAge:
                Intent ageIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                ageIntent.putExtra("what_info", "age");
                ageIntent.putExtra("user_data", user);
                startActivityForResult(ageIntent, 2);
//                startActivity(ageIntent);
                break;

            case R.id.rlSchool:
                Intent schoolIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                schoolIntent.putExtra("what_info", "school");
                schoolIntent.putExtra("user_data", user);
                startActivityForResult(schoolIntent, 2);
//                startActivity(schoolIntent);
                break;
            case R.id.rlEmail:
                Intent emailIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                emailIntent.putExtra("what_info", "email");
                emailIntent.putExtra("user_data", user);
                startActivityForResult(emailIntent, 2);
//                startActivity(emailIntent);
                break;
            case R.id.rlAdderes:
                Intent addressIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                addressIntent.putExtra("what_info", "address");
                addressIntent.putExtra("user_data", user);
                startActivityForResult(addressIntent, 2);
//                startActivity(addressIntent);
                break;
            case R.id.rlPostwd:
                Intent postwdIntent = new Intent(PersonInfoActivity.this, ModifyInfoActivity.class);
                postwdIntent.putExtra("what_info", "postwd");
                postwdIntent.putExtra("user_data", user);
                startActivityForResult(postwdIntent, 2);
//                startActivity(postwdIntent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (resultCode == RESULT_OK) {
                    user = (User) data.getSerializableExtra("user_data");
                    LogUtil.d("PersonInfoActivity", "info:" + user.getUsername());
                    initView();
                }
                break;
            default:
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
                            modifySex("男");
                        }
                        break;
                    case 1: // 女
                        if (!tvSex.getText().equals("女")) {
                            tvSex.setText("女");
                            modifySex("女");
                        }
                        break;
                }
            }

            private void modifySex(final String sex) {
                //保存数据库，网络请求
//                RequestBody requestBody = new FormBody.Builder()
//                        .add("phoneNumber", user.getTel())
//                        .add("sex", sex.equals("男") ? "0" : "1")
//                        .build();
//                HttpUtil.postCallback(requestBody, Constants.MODIFY_SEX_URL, new okhttp3.Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        LogUtil.d("PersonInfoActivity", e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String getReturn = response.body().string();
//                        LogUtil.d("PersonInfoActivity", getReturn);
//                        if (getReturn.equals("1")) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ToastUtil.showToast(PersonInfoActivity.this, "保存成功");
//                                }
//                            });
//                        } else {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ToastUtil.showToast(PersonInfoActivity.this, "保存失败");
//                                }
//                            });
//                        }
//                    }
//                });
                BmobQuery<User> query = new BmobQuery<User>();
                query.addWhereEqualTo("tel", user.getTel());
                query.findObjects(new FindListener<User>() {
                    @Override
                    public void done(List<User> object, BmobException e) {
                        if (e == null) {
                            String objectId = object.get(0).getObjectId();
                            user.setSex(sex.equals("男") ? "0" : "1");
                            user.update(objectId, new UpdateListener() {

                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        //更新成功
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showToast(PersonInfoActivity.this, "保存成功");
                                            }
                                        });
                                    }else{
                                        LogUtil.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ToastUtil.showToast(PersonInfoActivity.this, "保存失败");
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            LogUtil.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });


                if (sex.equals("男")) {
                    user.setSex("0");
                } else {
                    user.setSex("1");
                }
                //修改sharedpreferences中的信息
                clearData(PersonInfoActivity.this);
                saveObject(PersonInfoActivity.this, user);
            }
        });
        builder.create().show();
    }

}
