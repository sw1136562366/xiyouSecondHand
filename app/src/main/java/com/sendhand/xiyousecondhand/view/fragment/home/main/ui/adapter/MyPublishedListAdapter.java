package com.sendhand.xiyousecondhand.view.fragment.home.main.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.databinding.ItemHomeListBinding;
import com.sendhand.xiyousecondhand.databinding.MyPunlishedItemListBinding;
import com.sendhand.xiyousecondhand.view.fragment.home.bind.callback.RecyclerViewAdapterChangedCallback;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.HomeListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/7 0007.
 */

public class MyPublishedListAdapter extends BaseQuickAdapter<HomeListEntity, MyPublishedListAdapter.ViewHolder> {
    private Context mContext;
    private int layoutResId;

    public MyPublishedListAdapter(Context context, int layoutResId, List<HomeListEntity> data) {
        super(layoutResId, data);
        this.mContext = context;
        this.layoutResId = layoutResId;
    }

    @Override
    protected MyPublishedListAdapter.ViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        MyPunlishedItemListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                layoutResId,
                parent, false);
        MyPublishedListAdapter.ViewHolder holder = new MyPublishedListAdapter.ViewHolder(binding);
        return holder;
    }

    @Override
    protected void convert(MyPublishedListAdapter.ViewHolder holder, HomeListEntity item) {
        holder.mBinding.setEntity(item);
        if (null != item.getPhotoList()) {
            holder.setList(item.getPhotoList());
        }
    }

    @Override
    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }


    @Override
    public void onViewRecycled(MyPublishedListAdapter.ViewHolder holder) {
        if (null != holder.mBinding) {
            holder.mBinding.unbind();
        }
    }

    public class ViewHolder extends BaseViewHolder {

        MyPunlishedItemListBinding mBinding;

        private ObservableList<String> mList;
        private HomePhotoListAdapter mAdapter;

        public ViewHolder(View view) {
            super(view);
        }

        public ViewHolder(MyPunlishedItemListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;

            mList = new ObservableArrayList<>();
            mAdapter = new HomePhotoListAdapter(mContext, mList);
            LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            mBinding.list.setLayoutManager(manager);
            mBinding.list.setAdapter(mAdapter);

            mList.addOnListChangedCallback(new RecyclerViewAdapterChangedCallback(mAdapter));
        }

        public void setList(List<String> list) {
            mList.clear();
            mList.addAll(list);
        }
    }
}
