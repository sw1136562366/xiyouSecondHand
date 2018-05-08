package com.sendhand.xiyousecondhand.view.fragment.home.main.model.impl;

import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.entry.Goods;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.SharedPrefercesUtil;
import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.ViewLayer;
import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.ViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.HomeEntity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.HomeListEntity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.IMyPublishedViewModel;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public class MyPublishedViewModel extends ViewModel implements IMyPublishedViewModel {

    private ObservableList<HomeListEntity> myPublishedList;
    private static final int PAGE_SIZE = 10;
    private int myPoblishedListListPage = 1;
    private HomeEntity mEntity;

    public MyPublishedViewModel(ViewLayer viewLayer) {
        super(viewLayer);
    }

    @Override
    protected void onAttach() {
        initData();
    }

    @Override
    protected void onDetach() {

    }

    public void initData() {
        mEntity = new HomeEntity();
        mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_DEFAULT);
        myPublishedList = new ObservableArrayList<>();
    }

    @Override
    public void startRefresh(boolean notify) {
        mEntity.setRefreshing(true, notify);
    }

    @Override
    public void refreshData() {
        mEntity.setListType(HomeEntity.LIST_TYPE_FRESH);
        mEntity.setLoadingMoreStatus(LoadMoreView.STATUS_DEFAULT);
        mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_DEFAULT);
        myPoblishedListListPage = 1;

        loadMyPublishedData();
    }

    @Override
    public List<HomeListEntity> getHomeList() {
        return myPublishedList;
    }

    /**
     * 加载我发布的数据
     * @param entityList
     * @param page
     */
    private void loadMyPublishedList(final List<HomeListEntity> entityList, int page) {
        BmobQuery<Goods> query = new BmobQuery<>();
        query.addWhereEqualTo("user", SharedPrefercesUtil.readObject(MyApplication.getContext()));
        // 按时间降序查询
        query.order("-publishDate");
        //查询关联用户信息
        query.include("user");
        query.setSkip(page * PAGE_SIZE);
        // 设置每页数据个数
        query.setLimit(PAGE_SIZE);
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> goodsList, BmobException e) {
                if (e == null) {
                    if (goodsList.size() > 0) {
                        for (Goods good : goodsList) {
                            HomeListEntity entity = new HomeListEntity();
                            entity.setObjectId(good.getObjectId());
                            entity.setTel(good.getUser().getTel());
                            entity.setName(good.getUser().getUsername());
                            entity.setDesc(good.getDesc());
                            entity.setPrice(good.getPrice());
                            entity.setDate(good.getPublishDate().getDate());
                            entity.setStatus(good.getStatus() == 1 ? "已发布" : "已下架");
                            entity.setAddress(String.valueOf(good.getPosition().getLongitude() + "," + String.valueOf(good.getPosition().getLatitude())));
                            entity.setIconUrl(good.getUser().getPic() != null ? good.getUser().getPic().getFileUrl() : "");
//                                            List<String> list1 = new ArrayList<>();
//                                            for (int j = 0; j < 7; j++) {
//                                                list1.add(Uri.parse("res:///" + R.mipmap.ic_test1).toString());
//                                            }
//                                            entity.setPhotoList(list1);
                            entityList.add(entity);
                        }
                    }
                } else {
                    LogUtil.d("bmob", e.getMessage() + e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void loadMyPublishedData() {
        myPublishedList.clear();
        mEntity.setRefreshLoading(true);
        //Rxjava
        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        List<HomeListEntity> entityList = new ArrayList<>();
                        loadMyPublishedList(entityList, 0);
                        Thread.sleep(1000);
                        e.onNext(entityList);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HomeListEntity>>() {
                    @Override
                    public void onNext(List<HomeListEntity> homeListEntities) {
                        myPublishedList.clear();
                        myPublishedList.addAll(homeListEntities);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void loadMore() {
        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        final List<HomeListEntity> list = new ArrayList<>();

                        loadMyPublishedList(list, myPoblishedListListPage);

                        Thread.sleep(1000);
                        e.onNext(list);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HomeListEntity>>() {
                    @Override
                    public void onNext(List<HomeListEntity> homeListEntities) {
                        if (null != homeListEntities && homeListEntities.size() > 0) {
                            myPublishedList.addAll(homeListEntities);
                        }
                        if (null == homeListEntities
                                || homeListEntities.size() < PAGE_SIZE) {
                            mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_END);
                            if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                                mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        myPoblishedListListPage++;
                    }
                });
    }

    @Override
    public void addMyPublishedListChangedCallback(ObservableList.OnListChangedCallback callback) {
        addObservableListBinding(myPublishedList, callback);
    }

    @Override
    public void addHomeEntityChangedCallback(Observable.OnPropertyChangedCallback callback) {
        addObservableBinding(mEntity, callback);
    }
}
