package com.sendhand.xiyousecondhand.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.MD5Util;
import com.sendhand.xiyousecondhand.util.ToastUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.sendhand.xiyousecondhand.view.RegisterActivity.setEditTextInhibitInputSpace;
import static com.sendhand.xiyousecondhand.view.RegisterActivity.setEditTextInhibitInputSpeChat;

/**
 * 重置密码
 */
public class ModifyPwdActivity extends BaseActivity implements View.OnClickListener{

    private EditText etPassword;
    private EditText etConPassword;
    private  Button btnModifyPwd;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        //得到手机号
       phoneNumber = getIntent().getStringExtra("phoneNumber");
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConPassword = (EditText) findViewById(R.id.etConPassword);
        btnModifyPwd = (Button) findViewById(R.id.btnModifyPwd);
        btnModifyPwd.setOnClickListener(this);
        //设置按钮不可用
        btnModifyPwd.setEnabled(false);
        btnModifyPwd.setBackgroundColor(Color.GRAY);
        setUnUseBtn();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnModifyPwd:
                if (etConPassword.getText().toString().equals(etPassword.getText().toString())) {
                    //密码进行MD5加密
                    final String password = MD5Util.md5(etPassword.getText().toString());

                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("tel", phoneNumber);
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> object, BmobException e) {
                            if (e == null) {
                                String objectId = object.get(0).getObjectId();
                                User user = new User();
                                user.setPassword(password);
                                user.update(objectId, new UpdateListener() {

                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            //更新成功
                                            Intent intent = new Intent(ModifyPwdActivity.this, LoginActivity.class);
                                            intent.putExtra("phoneNumber", phoneNumber);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            LogUtil.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });
                            } else {
                                LogUtil.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                    //将密码传到服务端，保存
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("phoneNumber", phoneNumber)
//                            .add("password", password)
//                            .build();
//                    HttpUtil.postCallback(requestBody, Constants.RESETTING_PASSWORD_URL, new okhttp3.Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            //异常情况
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            //接收服务端返回数据,并解析
//
//                            Intent intent = new Intent(ModifyPwdActivity.this, LoginActivity.class);
//                            intent.putExtra("phoneNumber", phoneNumber);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
                } else {
                    ToastUtil.showToast(ModifyPwdActivity.this, "密码不一致，请重新设置！");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置注册按钮不可用户
     */
    private void setUnUseBtn() {
        //EditText监听器
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnModifyPwd.setEnabled(false);
                btnModifyPwd.setBackgroundColor(Color.GRAY);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etConPassword.getText().length() >= 6 && etPassword.getText().length() >= 6) {
                    btnModifyPwd.setEnabled(true);
                    btnModifyPwd.setBackgroundResource(R.drawable.login_button_back);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                setEditTextInhibitInputSpace(etPassword);
                setEditTextInhibitInputSpeChat(etConPassword);
            }
        };
        etConPassword.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
    }

}
