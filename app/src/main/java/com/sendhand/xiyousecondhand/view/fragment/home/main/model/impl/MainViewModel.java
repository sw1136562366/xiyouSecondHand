package com.sendhand.xiyousecondhand.view.fragment.home.main.model.impl;

import android.app.Application;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;

import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.ViewLayer;
import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.ViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.IMainViewModel;

import java.util.List;


public class MainViewModel extends ViewModel implements IMainViewModel {

    private ObservableList<IDrawerItem> mDrawerItems;
    private ObservableInt mCurrentIndex;

    public MainViewModel(ViewLayer viewLayer) {
        super(viewLayer);
    }

    @Override
    protected void onAttach() {
        initData();
    }

    @Override
    protected void onDetach() {
    }

    private void initData() {
        mDrawerItems = new ObservableArrayList<>();
        mCurrentIndex = new ObservableInt(-1);

        setDrawer();
    }

    @Override
    public List<IDrawerItem> getDrawerItems() {
        return mDrawerItems;
    }

    @Override
    public void addDrawerItemsChangedCallback(ObservableList.OnListChangedCallback callback) {
        addObservableListBinding(mDrawerItems, callback);
    }

    @Override
    public void addCurrentIndexChangedCallback(Observable.OnPropertyChangedCallback callback) {
        addObservableBinding(mCurrentIndex, callback);
    }

    @Override
    public void loadNavHeaderData() {

    }

    @Override
    public void loadDrawerItemData() {
    }

    @Override
    public void setIndex(int index) {
        if (index == mCurrentIndex.get()) {
            return;
        }
        mCurrentIndex.set(index);
    }

    private void setDrawer() {
        Application application = (Application) MyApplication.getContext();

        PrimaryDrawerItem item = new PrimaryDrawerItem()
                .withIcon(R.drawable.ic_menu_camera)
                .withIdentifier(1)
                .withName("Test 1")
                .withTextColorRes(R.color.colorTextNormal)
                .withSelectable(false)
                .withBadge("1,234.56")
                .withBadgeStyle(new BadgeStyle()
                        .withTextColorRes(R.color.colorPrimary));
        mDrawerItems.add(item);

        item = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName("Test 2")
                .withIcon(R.drawable.ic_menu_camera)
                .withTextColorRes(R.color.colorTextNormal)
                .withSelectable(false)
                .withBadgeStyle(new BadgeStyle(com.mikepenz.materialdrawer.R.drawable.material_drawer_badge,
                        application.getResources().getColor(R.color.colorPrimary),
                        application.getResources().getColor(R.color.colorPrimary),
                        application.getResources().getColor(R.color.colorWhite)))
                .withBadge("1个新回话");
        mDrawerItems.add(item);

        item = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName("Test 3")
                .withIcon(R.drawable.ic_menu_camera)
                .withTextColorRes(R.color.colorTextNormal)
                .withSelectable(false);
        mDrawerItems.add(item);
    }
}
