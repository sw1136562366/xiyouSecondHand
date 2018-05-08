package com.sendhand.xiyousecondhand.view.fragment.home.main.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.databinding.ItemShowActivityBinding;
import com.sendhand.xiyousecondhand.view.BaseActivity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.HomeListEntity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.adapter.HomePhotoListAdapter;

import io.rong.imkit.RongIM;

public class ItemShowActivity extends BaseActivity {

    private HomeListEntity itemInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_show_activity);
        itemInfo = (HomeListEntity) getIntent().getSerializableExtra("itemInfo");
        ItemShowActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.item_show_activity);
        binding.ivIcon.setImageURI(itemInfo.getIconUrl());
        binding.tvName.setText(itemInfo.getName());
        binding.tvDate.setText(itemInfo.getDate());
        binding.tvAddress.setText(itemInfo.getAddress());
        binding.tvPrice.setText(String.valueOf("¥" + itemInfo.getPrice()));
        binding.tvDesc.setText(itemInfo.getDesc());
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.list.setLayoutManager(manager);
        binding.list.setNestedScrollingEnabled(false);
        if (itemInfo.getPhotoList() != null && itemInfo.getPhotoList().size() > 0) {
            binding.list.setAdapter(new HomePhotoListAdapter(this, itemInfo.getPhotoList()));
        }
        binding.llTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(MyApplication.getContext(), itemInfo.getTel(), itemInfo.getName());
                }
            }
        });

        //返回按钮
        TextView btnBack = (TextView) findViewById(R.id.btnTopTitleLeft);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvTopTitleCenter = (TextView) findViewById(R.id.tvTopTitleCenter);
        tvTopTitleCenter.setText("商品详情");
    }
}
