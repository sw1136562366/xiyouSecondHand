package com.sendhand.xiyousecondhand.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sendhand.xiyousecondhand.R;
import com.sendhand.xiyousecondhand.MyApplication;
import com.sendhand.xiyousecondhand.entry.User;
import com.sendhand.xiyousecondhand.util.HttpUtil;
import com.sendhand.xiyousecondhand.util.LogUtil;
import com.sendhand.xiyousecondhand.util.ToastUtil;
import com.sendhand.xiyousecondhand.view.LoginActivity;
import com.sendhand.xiyousecondhand.view.MyPublishedActivity;
import com.sendhand.xiyousecondhand.view.PersonInfoActivity;
import com.sendhand.xiyousecondhand.view.SmsSendActivity;
import com.sendhand.xiyousecondhand.view.fragment.home.main.ui.MainActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.sendhand.xiyousecondhand.util.SharedPrefercesUtil.clearData;


public class PersonFragment extends Fragment implements View.OnClickListener{
    private ImageView pull_img;
    private ImageView profile_image;
    private User user;
    private String text;
    private TextView tvUserName;
    protected static final int CHOOSE_PICTURE = 1;
    protected static final int TAKE_PICTURE = 2;
    private static Uri imageUri;
    //拍照输出文件
    private File outputImage;
    //显示头像
    private  Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap)msg.obj;
            pull_img.setImageBitmap(bitmap);//将图片的流转换成图片
            profile_image.setImageBitmap(bitmap);
        }
    };

    public static PersonFragment newInstance() {
        PersonFragment fragment = new PersonFragment();
        return fragment;
    }

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
        initView(view);
        /**
         * 读取头像
         * 先从sharedpreferences读取，未读到再从本地读取，未读到再从服务器读取
         */
//        if (!getBitmapFromSharedPreferences()) {
            if (!readImage()) {
                //网络请求后台图片流
                getImageFromServer();
            }
//        }

        return view;
    }

    private void initView(View view) {
        //设置标题
//        TextView titleText = view.findViewById(R.id.tvTopTitleCenter);
//        titleText.setText("我的");
        tvUserName = view.findViewById(R.id.tvUserName);
        if (user != null) {
            tvUserName.setText(user.getUsername());
        }

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
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        user = ((MainActivity ) context).user;
    }

    @Override
    public void onStart() {
        super.onStart();
        user = ((MainActivity) getActivity()).user;
        tvUserName.setText(user.getUsername());
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
                editInfoIntent.putExtra("user_data",user);
                getActivity().startActivityForResult(editInfoIntent, 3);
                break;
            //修改密码
            case R.id.tvModifyPwd:
                Intent intent = new Intent(getActivity(), SmsSendActivity.class);
                intent.putExtra("sign", "modifyPassword");
                startActivity(intent);
                break;
            //我发布的
            case R.id.tvPublished:
                Intent intent1 = new Intent(getActivity(), MyPublishedActivity.class);
                startActivity(intent1);
                break;
            //我求购的
            case R.id.tvWillBuy:

                break;
            //登出
            case R.id.tvLoginout:
                logout();
                break;
            default:
                break;
        }
    }



    /**
     * 从服务器请求图片
     */
    private void getImageFromServer() {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("tel", user.getTel());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    if (object.size() > 0) {
                        if (object.get(0).getPic() != null) {
                            String picUrl = object.get(0).getPic().getFileUrl();
                            //根据图片url加载图片
                            HttpUtil.getCallback(picUrl, new okhttp3.Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    LogUtil.d("PersonFragment", e.getMessage());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    //得到图片的流
                                    InputStream inputStream = response.body().byteStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    Message message = new Message();
                                    message.obj = bitmap;
                                    handler.sendMessage(message);
                                }
                            });
                        }
                    }
                } else {
                    LogUtil.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

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
                    case 0: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case 1: // 拍照
                        outputImage = new File(getActivity().getExternalCacheDir(), "output_image.jpg");
                        try {
                            if (outputImage .exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (Build.VERSION.SDK_INT >= 24) {
                            imageUri = FileProvider.getUriForFile(getActivity(), "com.sendhand.xiyousecondhand.fileprovider", outputImage);
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
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("Main", "选择选择选择选择选择选择选择");
        switch (requestCode) {
            case CHOOSE_PICTURE:
                if (resultCode == RESULT_OK) {

                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4以上
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        pull_img.setImageBitmap(bitmap);
                        profile_image.setImageBitmap(bitmap);
                        //图片缓存到本地
                        saveImage(bitmap);
                        //图片上传到服务器
//                        saveBitmapToSharedPreferences(bitmap);
                        uploadPic(outputImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 3:
                LogUtil.d("PersonFragment", "*************");
                if (resultCode == RESULT_OK) {
                    user = (User) data.getSerializableExtra("user_data");
                    LogUtil.d("PersonFragment", user.getSchool());
//                    String text = data.getStringExtra("text");
//                    LogUtil.d("PersonFragment", text);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 上传头像
     * @param picFile
     */
    private void uploadPic(File picFile) {
//        String picPath = "sdcard/temp.jpg";
        final BmobFile bmobFile = new BmobFile(picFile);
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址

                    //头像0文件保存到Bmob
                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereEqualTo("tel", user.getTel());
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> object, BmobException e) {
                            if (e == null) {
                                String objectId = object.get(0).getObjectId();
                                user.setPic(bmobFile);
                                user.update(objectId, new UpdateListener() {

                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            //更新成功
                                            ToastUtil.showToast(getActivity(), "上传头像成功");
                                        }else{
                                            LogUtil.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });
                            } else {
                                LogUtil.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });

                    //刷新融云缓存中的用户信息
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(user.getTel(), user.getUsername(), Uri.parse(bmobFile.getFileUrl())));
                    }

                }else{
                    ToastUtil.showToast(getActivity(), "上传头像失败");
                    LogUtil.d("bmob", e.getMessage() + e.getErrorCode());
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("phoneNumber", user.getTel());
//        HttpUtil.post_file(Constants.UPLOAD_IMAGE_URL, map, pic, new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtil.d("PersonFragment", e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                LogUtil.d("PersonFragment", "*****************");
//                if (response.isSuccessful()) {
//                    String getReturn = response.body().string();
//                    LogUtil.d("PersonFragment", getReturn);
//                    if (getReturn.equals("1")) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtil.showToast(getActivity(), "上传成功");
//
//
//                                //TODO
//
//
//                                //刷新融云缓存中的用户信息
//                                if (RongIM.getInstance() != null) {
//                                    RongIM.getInstance().refreshUserInfoCache(new UserInfo("userId", "啊明", Uri.parse("http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png")));
//                                }
//
//                            }
//                        });
//                    } else {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtil.showToast(getActivity(), "上传失败");
//                            }
//                        });
//                    }
//                }
//            }
//        });
    }

    /**
     * 4.4以下处理图片方法
     * @param data
     */
    public void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 4.4以上处理图片方法
     * @param data
     */
    @TargetApi(19)
    public void handleImageOnKitKat(Intent data) {
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
    public void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            pull_img.setImageBitmap(bitmap);
            profile_image.setImageBitmap(bitmap);
            //图片缓存本地SD卡
            saveImage(bitmap);
            //图片保存到sharedPreferences，并且上传
//            saveBitmapToSharedPreferences(bitmap);
            uploadPic(new File(imagePath));

        } else {
            Toast.makeText(getActivity(), "获取图片路径失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登出
     */
    private void logout() {
        //弹出提示框
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("登出");
        builder.setMessage("确认退出登录?");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //清空sharedPreferences
                clearData(MyApplication.getContext());
                //跳转登录
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        builder.show();
    }



    /**
     * 申请权限
     */
    private void requestPermissions() {
        //运行时权限申请
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), permissionList.toArray(new String[permissionList.size()]), 10);
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
            case 10:
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
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            File file = new File(filesDir,user.getTel() + "icon.png");
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
        File file = new File(filesDir,user.getTel() != null ?  user.getTel() : "" + "icon.png");
        if(file.exists()){
            //存储--->内存
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            pull_img.setImageBitmap(bitmap);
            profile_image.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    //保存图片到SharedPreferences
    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        // Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_image", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();

        //上传头像
//        uploadImage(imageString,"");
    }


    /**
     *
     * @param imgStr
     * @param imgName
     * @return 返回头像地址
     */
    public String uploadImage(String imgStr, String imgName) {
//        RequestBody requestBody = new FormBody.Builder()
//                .add("phoneNumber", user.getTel())
//                .add("pic", imgStr)
//                .build();
//        HttpUtil.postCallback(requestBody, Constants.UPLOAD_IMAGE_URL, new okhttp3.Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//            }
//        });
        String picPath = "sdcard/temp.jpg";
        BmobFile bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    ToastUtil.showToast(getActivity(), "上传头像成功");
                }else{
                    ToastUtil.showToast(getActivity(), "上传头像失败");
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });

        return bmobFile.getFileUrl();
    }

    //从SharedPreferences获取图片
    private boolean getBitmapFromSharedPreferences(){
        boolean isGet = false;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_image", Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        String imageString=sharedPreferences.getString("image", "");
        //第二步:利用Base64将字符串转换为ByteArrayInputStream
        byte[] byteArray=Base64.decode(imageString, Base64.DEFAULT);
        if(byteArray.length !=0){
            isGet = true;
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);

            //第三步:利用ByteArrayInputStream生成Bitmap
            Bitmap bitmap= BitmapFactory.decodeStream(byteArrayInputStream);
            profile_image.setImageBitmap(bitmap);
            pull_img.setImageBitmap(bitmap);
        }
        return isGet;
    }

}