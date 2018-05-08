package com.sendhand.xiyousecondhand.view.fragment.home.main.ui.layer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.databinding.FragmentHomeBinding;
import com.sendhand.xiyousecondhand.databinding.MyPublishedActivityBinding;
import com.sendhand.xiyousecondhand.entry.Goods;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.ToastUtil;
import com.sendhand.xiyousecondhand.view.fragment.home.bind.callback.BaseQuickAdapterChangedCallback;
import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.DataBindingViewLayer;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.HomeListEntity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.IHomeViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.IMyPublishedViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.ItemShowActivity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.MainActivity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.adapter.HomeListAdapter;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.adapter.MyPublishedListAdapter;
import com.sendhand.xiyousecondhand.view.fragment.home.utils.DisplayUtil;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public class MyPublishedViewLayer extends DataBindingViewLayer<MyPublishedActivityBinding, IMyPublishedViewModel, AppCompatActivity> implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private IMyPublishedViewModel myPublishedViewModel;
    private FrameLayout mViewContainer;
    private RecyclerView mHomeList;
    private MyPublishedListAdapter mAdapter;

    public MyPublishedViewLayer(MyPublishedActivityBinding binding, AppCompatActivity activity) {
        super(binding, activity);
    }

    @Override
    public void onRefresh() {
//        myPublishedViewModel.startRefresh(false);
        myPublishedViewModel.refreshData();
    }

    @Override
    public void onLoadMoreRequested() {
        myPublishedViewModel.loadMore();
    }

    @Override
    protected void onAttach(IMyPublishedViewModel viewModel) {
        myPublishedViewModel = viewModel;
        myPublishedViewModel.loadMyPublishedData();
        initMyPublishedListView();
        myPublishedViewModel.addMyPublishedListChangedCallback(new BaseQuickAdapterChangedCallback(mAdapter));
    }

    @Override
    protected void onDetach() {
        super.onDetach();
    }

    public void initMyPublishedListView() {
        //设置SwipeRefresh布局
        mBinding.layoutRefresh.setColorSchemeResources(R.color.colorPrimary);
        mBinding.layoutRefresh.setOnRefreshListener(this);

        mViewContainer = mBinding.container;
        mHomeList = mBinding.list;

        mAdapter = new MyPublishedListAdapter(mContainer, R.layout.my_punlished_item_list, myPublishedViewModel.getHomeList());
        mAdapter.setOnLoadMoreListener(this, mHomeList);
        LinearLayoutManager manager = new LinearLayoutManager(mContainer);

        mHomeList.setLayoutManager(manager);
        mHomeList.setAdapter(mAdapter);

        //TODO 商品列表点击
        mHomeList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
//                ToastUtil.showToast(MyApplication.getContext(), position + mAdapter.getItem(position).getTel());
                HomeListEntity itemInfo = mAdapter.getItem(position);
                Intent intent = new Intent(MyApplication.getContext(), ItemShowActivity.class);
                intent.putExtra("itemInfo", itemInfo);
                MyApplication.getContext().startActivity(intent);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                final HomeListEntity itemInfo = mAdapter.getItem(position);
                final String[] items = new String[1];
                items[0] = itemInfo.getStatus().equals("已发布")? "下架" : "重新发布";
                AlertDialog.Builder builder = new AlertDialog.Builder(mContainer);
                builder.setTitle("修改商品状态");
                builder.setNegativeButton("取消", null);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0 :
                                        Goods good = new Goods();
                                        good.setStatus(items[0].equals("下架") ? 0 : 1);
                                        good.update(itemInfo.getObjectId(), new UpdateListener() {

                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                    ToastUtil.showToast(MyApplication.getContext(), items[0] + "成功");
                                                }else{
                                                    ToastUtil.showToast(MyApplication.getContext(), items[0] + "失败");
                                                }
                                            }

                                        });
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                builder.create().show();
            }
        });


    }
}
