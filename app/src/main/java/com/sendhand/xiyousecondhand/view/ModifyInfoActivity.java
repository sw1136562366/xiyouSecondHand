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
import com.sendhand.xiyousecondhand.entry.Constants;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.ToastUtil;

import java.lang.reflect.Method;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.sendhand.xiyousecondhand.util.SharedPrefercesUtil.clearData;
import static com.sendhand.xiyousecondhand.util.SharedPrefercesUtil.saveObject;

public class ModifyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUserInfo;
    private String what_info;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_info);
        what_info = getIntent().getStringExtra("what_info");
        user = (User) getIntent().getSerializableExtra("user_data");
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
            etUserInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (user.getAge() != null) {
                etUserInfo.setText(user.getAge() + "");
                etUserInfo.setSelection((user.getAge() + "").length());//将光标移至文字末尾
            }
        } else if (what_info.equals("school")) {
            tvTopTitleCenter.setText("院校名称");
            if (user.getSchool() != null) {
                etUserInfo.setText(user.getSchool());
                etUserInfo.setSelection(user.getSchool().length());//将光标移至文字末尾
            }
        } else if (what_info.equals("email")) {
            tvTopTitleCenter.setText("邮箱");
            etUserInfo.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            if (user.getEmail() != null) {
                etUserInfo.setText(user.getEmail());
                etUserInfo.setSelection(user.getEmail().length());//将光标移至文字末尾
            }
        } else if (what_info.equals("address")) {
            tvTopTitleCenter.setText("收货地址");
            if (user.getAddress() != null) {
                etUserInfo.setText(user.getAddress());
                etUserInfo.setSelection(user.getAddress().length());//将光标移至文字末尾
            }
        } else if (what_info.equals("postwd")) {
            tvTopTitleCenter.setText("邮编");
            etUserInfo.setInputType(InputType.TYPE_CLASS_NUMBER);
            if (user.getPostwd() != null) {
                etUserInfo.setText(user.getPostwd());
                etUserInfo.setSelection(user.getPostwd().length());//将光标移至文字末尾
            }

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
                            modifyInfo(Constants.MODIFY_USERNAME_URL,"username", etUserInfo.getText().toString());
                        }
                    } else {
                        ToastUtil.showToast(this, "输入不合法");
                    }
                } else if (what_info.equals("age")) {
                    if (etUserInfo.getText().length() <= 3) {
                        if (!etUserInfo.getText().equals(user.getAge())) {
                            user.setAge(Integer.parseInt(etUserInfo.getText().toString()));
                            //数据库修改
                            modifyInfo(Constants.MODIFY_AGE_URL,"age", etUserInfo.getText().toString());
                        }

                    } else {
                        ToastUtil.showToast(this, "输入不合法");
                    }
                } else if (what_info.equals("school")) {
                    if (!etUserInfo.getText().toString().contains(" ")) {
                        if (!etUserInfo.getText().equals(user.getSchool())) {
                            user.setSchool(etUserInfo.getText().toString());
                            //数据库修改
                            modifyInfo(Constants.MODIFY_SCHOOL_URL,"school", etUserInfo.getText().toString());
                        }
                    } else {
                        ToastUtil.showToast(this, "输入不合法");
                    }
                } else if (what_info.equals("email")) {
                    if (!etUserInfo.getText().equals(user.getEmail())) {
                        user.setEmail(etUserInfo.getText().toString());
                        //数据库修改
                        modifyInfo(Constants.MODIFY_EMAIL_URL,"email", etUserInfo.getText().toString());
                    }
                } else if (what_info.equals("address")) {
                    if (!etUserInfo.getText().equals(user.getAddress())) {
                        user.setAddress(etUserInfo.getText().toString());
                        //数据库修改
                        modifyInfo(Constants.MODIFY_ADDRESS_URL,"address", etUserInfo.getText().toString());
                    }
                } else if (what_info.equals("postwd")) {
                    if (!etUserInfo.getText().equals(user.getPostwd())) {
                        user.setPostwd(etUserInfo.getText().toString());
                        //数据库修改
                        modifyInfo(Constants.MODIFY_POSTWD_URL,"postwd", etUserInfo.getText().toString());
                    }
                }


                break;
            default:
                break;
        }
    }

    private void modifyInfo(String url, final String infoName, final String info) {
//        RequestBody requestBody = new FormBody.Builder()
//                .add("phoneNumber", user.getTel())
//                .add(infoName, info)
//                .build();
//        HttpUtil.postCallback(requestBody, url, new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtil.d("ModifyInfoActivity", e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String getReturn = response.body().string();
//                LogUtil.d("MofifyInfoActivity", getReturn);
//                if (getReturn.equals("1")) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ToastUtil.showToast(ModifyInfoActivity.this, "保存成功");
////                            if (infoName.equals("userName")) {
////                                user.setUsername(info);
//////                                User modifyedUser = new User();
//////                                modifyedUser.setUsername(info);
//////                                modifyedUser.setSex(user.getSex());
//////                                modifyedUser.setAge(user.getAge());
//////                                modifyedUser.setSchool(user.getSchool());
//////                                modifyedUser.setEmail(user.getEmail());
//////                                modifyedUser.set(user.getSex());
//////                                modifyedUser.setSex(user.getSex());
//////                                modifyedUser.setSex(user.getSex());
////                            } else if (infoName.equals("age")) {
////                                user.setAge(Integer.parseInt(info));
////                            } else if (infoName.equals("school")) {
////                                user.setSchool(info);
////                            } else if (infoName.equals("email")) {
////                                user.setEmail(info);
////                            } else if (infoName.equals("address")) {
////                                user.setAddress(info);
////                            } else if (infoName.equals("postwd")) {
////                                user.setPostwd(info);
////                            }
//                            LogUtil.d("MofifyInfoActivity", "name:" + user.getUsername());
//                        }
//                    });
//                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ToastUtil.showToast(ModifyInfoActivity.this, "保存失败");
//                        }
//                    });
//                }
//
//            }
//        });

        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("tel", user.getTel());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    String objectId = object.get(0).getObjectId();
                    Method[]  methods = user.getClass().getMethods();
                    for(int i = 0;i < methods.length; i++) {
                        if (("set" + infoName).toLowerCase().equals(methods[i].getName().toLowerCase())) {
                            try {
                                methods[i].invoke(user, info);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                    user.update(objectId, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                //更新成功
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast(ModifyInfoActivity.this, "保存成功");
                                    }
                                });
                            }else{
                                LogUtil.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast(ModifyInfoActivity.this, "保存失败");
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
        Intent intent = new Intent();
        intent.putExtra("user_data", user);
        setResult(RESULT_OK, intent);
//                            personInfoSign.finish();
//                            Intent intent = new Intent(ModifyInfoActivity.this, PersonInfoActivity.class);
//                            intent.putExtra("user_data", user);
//                            startActivity(intent);
        finish();
        //修改sharedpreferences中的信息
        clearData(this);
        saveObject(this, user);
    }
}
