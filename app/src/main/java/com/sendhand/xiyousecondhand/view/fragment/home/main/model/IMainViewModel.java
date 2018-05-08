package com.sendhand.xiyousecondhand.view.fragment.home.main.model;

import android.databinding.Observable;
import android.databinding.ObservableList;

import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.binding.IBinding;

import java.util.List;


public interface IMainViewModel extends IBinding {

    List<IDrawerItem> getDrawerItems();

    void addDrawerItemsChangedCallback(ObservableList.OnListChangedCallback callback);

    void addCurrentIndexChangedCallback(Observable.OnPropertyChangedCallback callback);

    /**
     * 设置侧边栏头部信息
     */
    void loadNavHeaderData();

    /**
     * 设置侧边栏条目信息
     */
    void loadDrawerItemData();

    /**
     * 设置当前页
     *
     * @param index
     */
    void setIndex(int index);
}
