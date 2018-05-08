package com.sendhand.xiyousecondhand.view.fragment.home.main.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.entry.Goods;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.ToastUtil;
import com.sendhand.xiyousecondhand.view.BaseActivity;
import com.sendhand.xiyousecondhand.view.PublishActivity;
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
            requestPermissions();

        }
    }

    public void requestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            Intent intent = new Intent(this, PublishActivity.class);
            intent.putExtra("user_data", user);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须允许所有权限才能使用该程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    Intent intent = new Intent(this, PublishActivity.class);
                    intent.putExtra("user_data", user);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
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
