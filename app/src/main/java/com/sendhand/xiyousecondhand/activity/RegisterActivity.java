package com.sendhand.xiyousecondhand.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.util.HttpUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sendhand.xiyousecondhand.R.drawable.login_button_back;

public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    private EditText etUserName;
    private EditText etPassword;
    private Button btnRegister;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        //设置按钮不可用
        btnRegister.setEnabled(false);
        btnRegister.setBackgroundColor(Color.GRAY);
        setUnUseBtn();
    }

    /**
     * 设置注册按钮不可用户
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
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                //向服务端发送数据
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("password", password);
                startActivity(intent);
                finish();
        }
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
