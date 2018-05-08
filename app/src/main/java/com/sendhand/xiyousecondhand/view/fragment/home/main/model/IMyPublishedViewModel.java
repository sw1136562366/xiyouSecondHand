package com.sendhand.xiyousecondhand.view.fragment.home.main.model;

import android.databinding.Observable;
import android.databinding.ObservableList;

import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.binding.IBinding;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.BannerEntity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.HomeListEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public interface IMyPublishedViewModel  extends IBinding {

    /**
     * 页面刷新
     */
    void startRefresh(boolean notify);


    /**
     * 刷新数据
     */
    void refreshData();

    /**
     * 获取已发布列表数据
     *
     * @return
     */
    List<HomeListEntity> getHomeList();

    /**
     * 刷新列表数据
     */
    void loadMyPublishedData();


    /**
     * 加载更多数据
     */
    void loadMore();

    /**
     * 对列表数据监听
     *
     * @param callback
     */
    void addMyPublishedListChangedCallback(ObservableList.OnListChangedCallback callback);

    void addHomeEntityChangedCallback(Observable.OnPropertyChangedCallback callback);
}
