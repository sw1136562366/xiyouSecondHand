package com.sendhand.xiyousecondhand.view.fragment.home.main.ui;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.entry.Goods;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.ToastUtil;
import com.sendhand.xiyousecondhand.view.BaseActivity;
import com.sendhand.xiyousecondhand.view.fragment.PersonFragment;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.IMainViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.model.impl.MainViewModel;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.layer.MainViewLayer;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;

import static com.sendhand.xiyousecondhand.util.SharedPrefercesUtil.readObject;


public class MainActivity extends BaseActivity {

    private IMainViewModel mViewModel;
    public User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new MainViewModel(new MainViewLayer(this));
        mViewModel.bind();

//        mViewModel.loadDrawerItemData();
        mViewModel.setIndex(MainViewLayer.TAB_INDEX_HOME);

        //获取用户数据
        if (getIntent() != null) {
            user = (User) getIntent().getSerializableExtra("user_data");
        } else {
            user = (User) readObject(this);
        }


    }



    @Override
    protected void onDestroy() {
        mViewModel.unbind();
        super.onDestroy();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.home) {
            mViewModel.setIndex(MainViewLayer.TAB_INDEX_HOME);
        } else if (v.getId() == R.id.find) {
            mViewModel.setIndex(MainViewLayer.TAB_INDEX_FIND);
        } else if (v.getId() == R.id.message) {
            mViewModel.setIndex(MainViewLayer.TAB_INDEX_MESSAGE);
        } else if (v.getId() == R.id.mine) {
            mViewModel.setIndex(MainViewLayer.TAB_INDEX_MINE);
        } else if (v.getId() == R.id.plus) {
//            ToastUtil.showToast(this, "发布");
            insertGoods();
        }
    }


    private void insertGoods() {
        List<BmobObject> goods = new ArrayList<BmobObject>();
        for (int i = 1; i < 11; i++) {
            Goods good = new Goods();
            good.setTitle("商品标题" + i);
            good.setDesc("商品描述信息" + i);
            good.setUser(user);
            good.setPrice(Double.valueOf(100 + i));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            good.setPublishDate(new BmobDate(new Date()));
            good.setPosition(new BmobGeoPoint(116.39727786183357, 39.913768382429105));
            good.setStatus(1);
            goods.add(good);
        }
        new BmobBatch().insertBatch(goods).doBatch(new QueryListListener<BatchResult>() {

            @Override
            public void done(List<BatchResult> o, BmobException e) {
                if(e==null){
                    for(int i = 0; i < o.size(); i++){
                        BatchResult result = o.get(i);
                        BmobException ex =result.getError();
                        if(ex==null){
                            LogUtil.d("MainActivity", "第"+i+"个数据批量添加成功：");
                        }else{
                            LogUtil.d("MainActivity", "第"+i+"个数据批量添加失败：");
                        }
                    }
                    ToastUtil.showToast(MyApplication.getContext(), "添加成功");
                }else{
                    LogUtil.d("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
         /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
//        Fragment f = getSupportFragmentManager().findFragmentByTag("personFragment");
//        /*然后在碎片中调用重写的onActivityResult方法*/
//        f.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 3:
                if (resultCode == RESULT_OK) {
                    user = (User) data.getSerializableExtra("user_data");
//                    LogUtil.d("MainActivity", user.getSchool());
                }
                break;
            default:
        }
    }

}
