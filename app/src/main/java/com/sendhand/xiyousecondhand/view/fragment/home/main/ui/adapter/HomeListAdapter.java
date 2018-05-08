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
import com.sendhand.xiyousecondhand.view.fragment.home.bind.callback.RecyclerViewAdapterChangedCallback;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.HomeListEntity;

import java.util.List;

public class HomeListAdapter extends BaseQuickAdapter<HomeListEntity, HomeListAdapter.ViewHolder> {

    private Context mContext;
    private int layoutResId;

    public HomeListAdapter(Context context, int layoutResId, List<HomeListEntity> data) {
        super(layoutResId, data);
        this.mContext = context;
        this.layoutResId = layoutResId;
    }

    @Override
    protected ViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        ItemHomeListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                layoutResId,
                parent, false);
        ViewHolder holder = new ViewHolder(binding);
        return holder;
    }

    @Override
    protected void convert(ViewHolder holder, HomeListEntity item) {
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
    protected ViewHolder createBaseViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        if (null != holder.mBinding) {
            holder.mBinding.unbind();
        }
    }

    public class ViewHolder extends BaseViewHolder {

        ItemHomeListBinding mBinding;

        private ObservableList<String> mList;
        private HomePhotoListAdapter mAdapter;

        public ViewHolder(View view) {
            super(view);
        }

        public ViewHolder(ItemHomeListBinding binding) {
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
