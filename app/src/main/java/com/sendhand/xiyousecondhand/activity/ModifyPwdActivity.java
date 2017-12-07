package com.sendhand.xiyousecondhand.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sendhand.xiyousecondhand.R;

import static com.sendhand.xiyousecondhand.activity.RegisterActivity.setEditTextInhibitInputSpace;
import static com.sendhand.xiyousecondhand.activity.RegisterActivity.setEditTextInhibitInputSpeChat;

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
                    //将密码传到服务端，保存

                    Intent intent = new Intent(ModifyPwdActivity.this, LoginActivity.class);
                    intent.putExtra("phoneNumber", phoneNumber);
                    intent.putExtra("password", etPassword.getText());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ModifyPwdActivity.this, "密码不一致，请重新设置！", Toast.LENGTH_SHORT).show();
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
