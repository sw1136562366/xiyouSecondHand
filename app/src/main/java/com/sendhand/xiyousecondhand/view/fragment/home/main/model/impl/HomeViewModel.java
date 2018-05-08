package com.sendhand.xiyousecondhand.view.fragment.home.main.model.impl;

import android.content.res.Resources;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.facebook.common.util.UriUtil;
import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.entry.Goods;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.ToastUtil;
import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.ViewLayer;
import com.sendhand.xiyousecondhand.view.fragment.home.core.mvvm.ViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.BannerEntity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.FunctionItemEntity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.HomeEntity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.entity.HomeListEntity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.IHomeViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.utils.ImageUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.amap.api.mapcore2d.p.i;


public class HomeViewModel extends ViewModel implements IHomeViewModel {

    private HomeEntity mEntity;

    private ObservableList<HomeListEntity> mHomeList;
    private List<HomeListEntity> mFreshList;

    private List<HomeListEntity> mNearList;
    private ObservableList<BannerEntity> mBannerList;

    private ObservableList<FunctionItemEntity> mFunctionList;

    private int mRefreshListPage = 1;
    private int mNearListPage = 1;

    private static final int PAGE_SIZE = 10;


    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多

    public HomeViewModel(ViewLayer viewLayer) {
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
        mEntity = new HomeEntity();
        mBannerList = new ObservableArrayList<>();
        mHomeList = new ObservableArrayList<>();
        mFreshList = new ArrayList<>();
        mNearList = new ArrayList<>();

        mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_DEFAULT);
        mEntity.setNearMoreStatus(LoadMoreView.STATUS_DEFAULT);
//        initFunctionEntities();
    }

    private void initFunctionEntities() {
        mFunctionList = new ObservableArrayList<>();
//        String iconUrl =
        for (int i = 0; i < 6; i++) {
            FunctionItemEntity entity = new FunctionItemEntity();
//            entity.setIconUrl(iconUrl);
            entity.setTitle("测试标题" + i);
            entity.setDesc("测试描述" + i);
            mFunctionList.add(entity);
        }
    }

    @Override
    public void startRefresh(boolean notify) {
        mEntity.setRefreshing(true, notify);
    }

    @Override
    public ObservableList<BannerEntity> getBannerList() {
        return mBannerList;
    }

    @Override
    public void refreshData() {
        resetData();
        loadBanner();
        loadHomeData();
    }

    private void resetData() {
        mEntity.setListType(HomeEntity.LIST_TYPE_FRESH);
        mRefreshListPage = 1;
        mNearListPage = 1;
        mEntity.setLoadingMoreStatus(LoadMoreView.STATUS_DEFAULT);
        mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_DEFAULT);
        mEntity.setNearMoreStatus(LoadMoreView.STATUS_DEFAULT);
    }

    /**
     * 加载轮播图
     */
    @Override
    public void loadBanner() {
        Uri[] bannerList = {
                Uri.parse("res:///" + R.mipmap.banner_5),
                Uri.parse("res:///" + R.mipmap.banner_6),
                Uri.parse("res:///" + R.mipmap.banner_7),
                Uri.parse("res:///" + R.mipmap.banner_8)
//                ImageUtil.getUriFromDrawableRes(R.mipmap.banner_5),
//                ImageUtil.getUriFromDrawableRes(R.mipmap.banner_6),
//                ImageUtil.getUriFromDrawableRes(R.mipmap.banner_7),
//                ImageUtil.getUriFromDrawableRes(R.mipmap.banner_8)
        };

        List<BannerEntity> banners = new ArrayList<>();
        for (int i = 0; i < bannerList.length; i++) {
            BannerEntity entity = new BannerEntity();
            entity.setImageUrl(bannerList[i].toString());
            banners.add(entity);
        }

        mBannerList.clear();
        mBannerList.addAll(banners);

        mEntity.setBannerCount(mBannerList.size());
    }

    @Override
    public void addBannerListChangedCallback(ObservableList.OnListChangedCallback callback) {
        addObservableListBinding(mBannerList, callback);
    }

    @Override
    public void addHomeEntityChangedCallback(Observable.OnPropertyChangedCallback callback) {
        addObservableBinding(mEntity, callback);
    }

    /**
     * 轮播图点击事件
     * @param entity
     */
    public void onBannerItemClick(BannerEntity entity) {
        //TODO 事件处理
    }

//    @Override
//    public List<FunctionItemEntity> getFunctionList() {
////        return mFunctionList;
//    }

//    @Override
    public void addFunctionListChangedCallback(ObservableList.OnListChangedCallback callback) {
//        addObservableListBinding(mFunctionList, callback);
    }

//    public void onFunctionItemClick(FunctionItemEntity entity) {
//        //TODO 事件处理
//    }

    @Override
    public List<HomeListEntity> getHomeList() {
        return mHomeList;
    }

    @Override
    public void addHomeListChangedCallback(ObservableList.OnListChangedCallback callback) {
        addObservableListBinding(mHomeList, callback);
    }

    /**
     * 加载'新鲜的'列表数据
     * @param entityList
     */
    private void loadRefresData(final List<HomeListEntity> entityList, int page) {
        BmobQuery<Goods> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-publishDate");
        query.addWhereEqualTo("status", 1);
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
                            entity.setTel(good.getUser().getTel());
                            entity.setName(good.getUser().getUsername());
                            entity.setDesc(good.getDesc());
                            entity.setPrice(good.getPrice());
                            entity.setDate(good.getPublishDate().getDate());
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

    /**
     * 加载附近的
     * @param entityList
     * @param page
     */
    private void loadNearData(final List<HomeListEntity> entityList, int page) {
        BmobQuery<Goods> query = new BmobQuery<>();
        // 位置查询
//        query.addWhereNear("position", new BmobGeoPoint(112.934755, 24.52065));
        //查询关联用户信息
        query.include("user");
        query.addWhereEqualTo("status", 1);
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
                            entity.setTel(good.getUser().getTel());
                            entity.setName(good.getUser().getUsername());
                            entity.setDesc(good.getDesc());
                            entity.setPrice(good.getPrice());
                            entity.setDate(good.getPublishDate().getDate());
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

    /**
     * 加载列表数据
     */
    @Override
    public void loadHomeData() {
        mHomeList.clear();
        mEntity.setRefreshLoading(true);

        //Rxjava
        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        final List<HomeListEntity> entityList = new ArrayList<>();

                        loadRefresData(entityList, 0);

                        Thread.sleep(2000);
                        e.onNext(entityList);
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<HomeListEntity>>() {
                    @Override
                    public void onNext(List<HomeListEntity> homeListEntities) {
                        mFreshList.clear();
                        mNearList.clear();
                        mFreshList.addAll(homeListEntities);
                        mHomeList.clear();
                        mHomeList.addAll(mFreshList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mEntity.isRefreshLoading()) {
                            mEntity.setRefreshLoading(false);
                        }
                        mEntity.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        if (mEntity.isRefreshLoading()) {
                            mEntity.setRefreshLoading(false);
                        }
                        mEntity.setRefreshing(false);
                    }
                });
    }

    @Override
    public void changeHomeData(int position) {
        int type = position;
        if (type == mEntity.getListType()) {
            return;
        }

        mEntity.setNearLoading(false);
        mEntity.setRefreshLoading(false);
        mEntity.setListType(type, false);
        if (type == HomeEntity.LIST_TYPE_FRESH) {
            if (mFreshList.size() > 0) {
                mHomeList.clear();
                mHomeList.addAll(mFreshList);
                mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus());
            } else {
                loadRefreshList();
            }
        } else {
            if (mNearList.size() > 0) {
                mHomeList.clear();
                mHomeList.addAll(mNearList);
                mEntity.setLoadingMoreStatus(mEntity.getNearMoreStatus());
            } else {
                loadNearList();
            }
        }
    }

    @Override
    public void loadMore() {
        if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
            loadMoreRefreshList();
        } else {
            loadMoreNearList();
        }
    }

    private void loadRefreshList() {
        if (mEntity.isRefreshLoading()) {
            return;
        }
        mHomeList.clear();
        mEntity.setRefreshLoading(true);

        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        List<HomeListEntity> entityList = new ArrayList<>();
                        loadRefresData(entityList, 0);
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
                        mFreshList.clear();
                        mFreshList.addAll(homeListEntities);

                        if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                            mHomeList.addAll(mFreshList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mEntity.isRefreshLoading()) {
                            mEntity.setRefreshLoading(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mEntity.isRefreshLoading()) {
                            mEntity.setRefreshLoading(false);
                        }
                    }
                });
    }

    private void loadNearList() {
        if (mEntity.isNearLoading()) {
            return;
        }
        mHomeList.clear();
        mEntity.setNearLoading(true);

        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        List<HomeListEntity> list = new ArrayList<>();
                        loadNearData(list, 0);
//                        String message = "附近的 ";
//                        for (int i = 1; i <= PAGE_SIZE; i++) {
//                            HomeListEntity entity = new HomeListEntity();
//                            entity.setTel("15829211215");
//                            entity.setName("昵称" + i);
//                            entity.setDesc("商品描述" + i);
//                            entity.setPrice(188.8);
//                            entity.setCommentCount(i);
//                            entity.setLikeCount(i);
//                            entity.setDate(new Date().toString());
//                            entity.setAddress("发布人地址" + i);
//                            entity.setGroupName(message + i);
//                            entity.setIconUrl(Uri.parse("res:///" + R.mipmap.ic_default_icon).toString());
//                            List<String> list1 = new ArrayList<>();
//                            for (int j = 0; j < 7; j++) {
//                                list1.add(Uri.parse("res:///" + R.mipmap.ic_test1).toString());
//                            }
//                            entity.setLiked(i % 2 == 0 ? true : false);
//                            entity.setPhotoList(list1);
//                            list.add(entity);
//                        }

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
                        mNearList.clear();
                        mNearList.addAll(homeListEntities);

                        if (HomeEntity.LIST_TYPE_NEAR == mEntity.getListType()) {
                            mHomeList.clear();
                            mHomeList.addAll(mNearList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mEntity.isNearLoading()) {
                            mEntity.setNearLoading(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mEntity.isNearLoading()) {
                            mEntity.setNearLoading(false);
                        }
                    }
                });
    }

    private void loadMoreRefreshList() {
        mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_LOADING);
        mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus(), false);

        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                       final List<HomeListEntity> list = new ArrayList<>();

                        loadRefresData(list, mRefreshListPage);
                        // 上拉加载更多(加载下一页数据)
//                        int size;
//                        if (mRefreshListPage > 2) {
//                            size = mRefreshListPage * PAGE_SIZE + 5;
//                        } else {
//                            size = (mRefreshListPage + 1) * PAGE_SIZE;
//                        }
//                        for (int i = mRefreshListPage * PAGE_SIZE + 1; i <= size; i++) {
//                            HomeListEntity entity = new HomeListEntity();
////                            entity.setTel("15829211215");
////                            entity.setName("昵称" + i);
////                            entity.setDesc("商品描述" + i);
////                            entity.setPrice(188.8);
////                            entity.setCommentCount(i);
////                            entity.setLikeCount(i);
////                            entity.setDate(new Date().toString());
////                            entity.setAddress("发布人地址" + i);
////                            entity.setGroupName(message + i);
////                            entity.setIconUrl(Uri.parse("res:///" + R.mipmap.ic_default_icon).toString());
////                            List<String> list1 = new ArrayList<>();
////                            for (int j = 0; j < 7; j++) {
////                                list1.add(Uri.parse("res:///" + R.mipmap.ic_test1).toString());
////                            }
////                            entity.setLiked(i % 2 == 0 ? true : false);
////                            entity.setPhotoList(list1);
////                            list.add(entity);
//                        }

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
                            mFreshList.addAll(homeListEntities);

                            if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                                mHomeList.addAll(homeListEntities);
                            }
                        }

                        if (null == homeListEntities
                                || homeListEntities.size() < PAGE_SIZE) {
                            mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_END);
                            if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                                mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus());
                            }
                        } else {
                            mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_DEFAULT);
                            if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                                mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mEntity.setRefreshMoreStatus(LoadMoreView.STATUS_FAIL);
                        if (HomeEntity.LIST_TYPE_FRESH == mEntity.getListType()) {
                            mEntity.setLoadingMoreStatus(mEntity.getRefreshMoreStatus());
                        }
                    }

                    @Override
                    public void onComplete() {
                        mRefreshListPage++;
                    }
                });
    }

    /**
     * 加载附近的商品列表数据
     */
    private void loadMoreNearList() {
        mEntity.setNearMoreStatus(LoadMoreView.STATUS_LOADING);
        mEntity.setLoadingMoreStatus(mEntity.getNearMoreStatus(), false);

        io.reactivex.Observable
                .create(new ObservableOnSubscribe<List<HomeListEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<HomeListEntity>> e) throws Exception {
                        List<HomeListEntity> list = new ArrayList<>();
                        loadNearData(list, mNearListPage);
//                        String message = "附近的 ";
//                        int size;
//                        if (mNearListPage > 2) {
//                            size = mNearListPage * PAGE_SIZE + 5;
//                        } else {
//                            size = (mNearListPage + 1) * PAGE_SIZE;
//                        }
//                        for (int i = mNearListPage * PAGE_SIZE + 1; i <= size; i++) {
//                            HomeListEntity entity = new HomeListEntity();
////                            entity.setTel("15829211215");
////                            entity.setName("昵称" + i);
////                            entity.setDesc("商品描述" + i);
////                            entity.setPrice(188.8);
////                            entity.setCommentCount(i);
////                            entity.setLikeCount(i);
////                            entity.setDate(new Date().toString());
////                            entity.setAddress("发布人地址" + i);
////                            entity.setGroupName(message + i);
////                            entity.setIconUrl(Uri.parse("res:///" + R.mipmap.ic_default_icon).toString());
////                            List<String> list1 = new ArrayList<>();
////                            for (int j = 0; j < 7; j++) {
////                                list1.add(Uri.parse("res:///" + R.mipmap.ic_test1).toString());
////                            }
////                            entity.setLiked(i % 2 == 0 ? true : false);
////                            entity.setPhotoList(list1);
////                            list.add(entity);
//                        }

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
                            mNearList.addAll(homeListEntities);

                            if (HomeEntity.LIST_TYPE_NEAR == mEntity.getListType()) {
                                mHomeList.addAll(homeListEntities);
                            }
                        }

                        if (null == homeListEntities
                                || homeListEntities.size() < PAGE_SIZE) {
                            mEntity.setNearMoreStatus(LoadMoreView.STATUS_END);
                            if (HomeEntity.LIST_TYPE_NEAR == mEntity.getListType()) {
                                mEntity.setLoadingMoreStatus(mEntity.getNearMoreStatus());
                            }
                        } else {
                            mEntity.setNearMoreStatus(LoadMoreView.STATUS_DEFAULT);
                            if (HomeEntity.LIST_TYPE_NEAR == mEntity.getListType()) {
                                mEntity.setLoadingMoreStatus(mEntity.getNearMoreStatus());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mEntity.setNearMoreStatus(LoadMoreView.STATUS_FAIL);
                        if (HomeEntity.LIST_TYPE_NEAR == mEntity.getListType()) {
                            mEntity.setLoadingMoreStatus(mEntity.getNearMoreStatus());
                        }
                    }

                    @Override
                    public void onComplete() {
                        mNearListPage++;
                    }
                });
    }
}
