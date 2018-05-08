package com.sendhand.xiyousecondhand.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.databinding.MyPublishedActivityBinding;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.IMyPublishedViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.impl.MyPublishedViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.adapter.HomeListAdapter;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.layer.MyPublishedViewLayer;

public class MyPublishedActivity extends BaseActivity {

    private MyPublishedViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyPublishedActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.my_published_activity);
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
        tvTopTitleCenter.setText("我发布的");

        MyPublishedViewLayer myPublishedViewLayer = new MyPublishedViewLayer(binding, MyPublishedActivity.this);
        viewModel = new MyPublishedViewModel(myPublishedViewLayer);
        viewModel.bind();
//        RecyclerView recyclerView = findViewById(R.id.list);
//        recyclerView.setAdapter(new HomeListAdapter(this, R.layout.my_punlished_item_list, mViewModel.getHomeList()););
    }

    @Override
    protected void onDestroy() {
        viewModel.unbind();
        finish();
        super.onDestroy();
    }
}
