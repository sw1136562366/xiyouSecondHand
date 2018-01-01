package com.sendhand.xiyousecondhand.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.BitmapBlurUtil;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.ToastUtil;
import com.sendhand.xiyousecondhand.view.MainActivity;
import com.sendhand.xiyousecondhand.view.ModifyPwdActivity;
import com.sendhand.xiyousecondhand.view.PersonInfoActivity;
import com.sendhand.xiyousecondhand.view.SmsSendActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class PersonFragment extends Fragment implements View.OnClickListener{
    private ImageView pull_img;
    private ImageView profile_image;
    private User user;
    private String text;
    private TextView tvUserName;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri imageUri;

    public PersonFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person,null);
        //设置标题
//        TextView titleText = view.findViewById(R.id.tvTopTitleCenter);
//        titleText.setText("我的");
        TextView tvUserName = view.findViewById(R.id.tvUserName);
//        tvUserName.setText(user.getUsername());
        pull_img = view.findViewById(R.id.pull_img);
        profile_image = view.findViewById(R.id.profile_image);
        TextView tvEditUserInfo = view.findViewById(R.id.tvEditUserInfo);
        TextView tvModifyPwd = view.findViewById(R.id.tvModifyPwd);
        TextView tvPublished = view.findViewById(R.id.tvPublished);
        TextView tvWillBuy = view.findViewById(R.id.tvWillBuy);
        TextView tvLoginout = view.findViewById(R.id.tvLoginout);

        tvModifyPwd.setOnClickListener(this);
        tvEditUserInfo.setOnClickListener(this);
        tvPublished.setOnClickListener(this);
        tvWillBuy.setOnClickListener(this);
        tvLoginout.setOnClickListener(this);
        profile_image.setOnClickListener(this);


        /**
         * 读取头像
         * 先从本地读取，未读到再从服务器读取
         */
        if (!readImage()) {
            //向服务器发起网络请求

        }


        //大头像
//        Resources res = getResources();
//        //读取用户头像
//        Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.image);
//        BitmapBlurUtil.addTask(bmp, new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                Drawable drawable = (Drawable) msg.obj;
//                pull_img.setImageDrawable(drawable);
//
//            }
//        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        user = ((MainActivity) context).user;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //设置头像
            case R.id.profile_image:
                requestPermissions();
                break;
            //编辑资料
            case R.id.tvEditUserInfo:
                Intent editInfoIntent = new Intent(getActivity(), PersonInfoActivity.class);
                user = new User();
                user.setUsername("啦啦啦");
                user.setAge(22);
                user.setSex("男");
                user.setAddress("陕西省西安市长安区西安邮电大学东区");
                user.setEmail("1136562366@qq.com");
                user.setPostwd("714300");
                user.setSchool("西安邮电大学");
                editInfoIntent.putExtra("user_data",user);
                startActivity(editInfoIntent);
                break;
            //修改密码
            case R.id.tvModifyPwd:
                Intent intent = new Intent(getActivity(), SmsSendActivity.class);
                intent.putExtra("sign", "modifyPassword");
                startActivity(intent);
                break;
            //我发布的
            case R.id.tvPublished:

                break;
            //我求购的
            case R.id.tvWillBuy:

                break;
            //登出
            case R.id.tvLoginout:

                break;
            default:
                break;
        }
    }

    /**
     * 显示修改头像的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        File outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
                        try {
                            if (outputImage .exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(getActivity(), "com.secondhand.xiyousecondhand.fileprovider", outputImage);
                        } else {
                            imageUri = Uri.fromFile(outputImage);
                        }
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        pull_img.setImageBitmap(bitmap);
                        profile_image.setImageBitmap(bitmap);
                        //图片缓存
                        saveImage(bitmap);
                        //图片上传到服务器

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PICTURE:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4以上
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
            default:
                break;
        }
    }

    /**
     * 4.4以下处理图片方法
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 4.4以上处理图片方法
     * @param data
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    /**
     * 获得图片的真实路径
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path= null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 根据图片真实路径，将图片显示到界面上
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            pull_img.setImageBitmap(bitmap);
            profile_image.setImageBitmap(bitmap);
            //图片缓存
            saveImage(bitmap);
            //图片上传到服务器


        } else {
            Toast.makeText(getActivity(), "获取图片路径失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 申请权限
     */
    private void requestPermissions() {
        //运行时权限申请
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            showChoosePicDialog();
        }
    }

    /**
     * 处理权限申请结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            ToastUtil.showToast(getActivity(), "必须开启所有权限");
                            return;
                        }
                    }
                    showChoosePicDialog();
                } else {
                    ToastUtil.showToast(getActivity(), "发生未知错误");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 数据的存储。（5种）
     * Bimap:内存层面的图片对象。
     *
     * 存储--->内存：
     *      BitmapFactory.decodeFile(String filePath);
     *      BitmapFactory.decodeStream(InputStream is);
     * 内存--->存储：
     *      bitmap.compress(Bitmap.CompressFormat.PNG,100,OutputStream os);
     */
    private void saveImage(Bitmap bitmap) {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = getActivity().getExternalFilesDir("");

        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = getActivity().getFilesDir();

        }
        FileOutputStream fos = null;
        try {
            File file = new File(filesDir,"icon.png");
            fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //如果本地有,就不需要再去联网去请求
    private boolean readImage() {
        File filesDir;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = getActivity().getExternalFilesDir("");

        }else{//手机内部存储
            //路径：data/data/包名/files
            filesDir = getActivity().getFilesDir();

        }
        File file = new File(filesDir,"icon.png");
        if(file.exists()){
            //存储--->内存
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            pull_img.setImageBitmap(bitmap);
            profile_image.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }
}