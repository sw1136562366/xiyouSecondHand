package com.sendhand.xiyousecondhand.view.fragment.home.main.ui;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sendhand.xiyousecondhand.R;

public class ConversationActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);
        String userPhone = getIntent().getData().getQueryParameter("targetId");
        String title = getIntent().getData().getQueryParameter("title");
        TextView tvTopTitleCenter = (TextView) findViewById(R.id.tvTopTitleCenter);
        if (!TextUtils.isEmpty(title)) {
            tvTopTitleCenter.setText(title);
        } else {
            tvTopTitleCenter.setText(userPhone);
        }
        TextView btnBack = (TextView) findViewById(R.id.btnTopTitleLeft);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
