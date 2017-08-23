package com.nx.httplibrarytest.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nx.commonlibrary.BaseActivity.BaseActivity;
import com.nx.httplibrary.NXHttpManager;
import com.nx.httplibrary.okhttp.cache.CacheMode;
import com.nx.httplibrary.okhttp.callback.FileCallback;
import com.nx.httplibrary.okhttp.callback.JsonCallback;
import com.nx.httplibrary.okhttp.model.Progress;
import com.nx.httplibrary.okhttp.model.Response;
import com.nx.httplibrary.okhttp.utils.OkLogger;
import com.nx.httplibrarytest.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @类描述： 上传文件页面
 * @创建人：王成丞
 * @创建时间：2017/8/10 15:39
 */
public class UpLoadActivity extends BaseActivity {

    private ImageView ivPic;
    private Button mBtDownload;
    private SeekBar seekbarUpload;
    private SeekBar seekbarDownload;
    private String token;
    private TextView tvUploadState;
    private TextView tvDownloadState;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload;
    }

    @Override
    public void initView() {


        ivPic = (ImageView) findViewById(R.id.iv_pic);
        mBtDownload = (Button) findViewById(R.id.bt_download);
        seekbarUpload = (SeekBar) findViewById(R.id.seekbar_upload);
        seekbarDownload = (SeekBar) findViewById(R.id.seekbar_download);
        tvUploadState = (TextView) findViewById(R.id.upload_state);
        tvDownloadState = (TextView) findViewById(R.id.download_state);

    }

    @Override
    protected void initHeadData() {

    }

    @Override
    protected void initListener() {
        ivPic.setOnClickListener(this);
        mBtDownload.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        token = intent.getStringExtra("token");


    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {


            case R.id.bt_download:
                //下载

                downLoadTest();


                break;
            case R.id.iv_pic:
                //图库选择图片进行上传
                ImagePicker imagePicker = ImagePicker.getInstance();
                imagePicker.setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {

                        Glide.with(activity).load(new File(path))//
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//
                                .into(imageView);
                    }

                    @Override
                    public void clearMemoryCache() {

                    }
                });
                imagePicker.setMultiMode(true);   //单选
                imagePicker.setShowCamera(true);  //显示拍照按钮
                imagePicker.setSelectLimit(9);    //最多选择9张
                imagePicker.setCrop(false);       //不进行裁剪
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }

    /**
     * 下载
     */
    private void downLoadTest() {


        NXHttpManager.<File>get("http://test-up.limingjie.top/niutan")
                .cacheMode(CacheMode.NO_CACHE)

                .execute(new FileCallback("/sdcard/", "niutan.apk") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        tvDownloadState.setText("下载状态：成功");

                    }

                    @Override
                    public void onError(Response<File> response) {
                        tvDownloadState.setText("下载状态：失败");

                        super.onError(response);
                    }


                    @Override
                    public void downloadProgress(Progress progress) {

                        tvDownloadState.setText("下载状态：正在下载   " + (progress.currentSize / 1024.0f) + " kb/" + (progress.totalSize / 1024.0f) + " kb");

                        seekbarDownload.setMax(100);
                        seekbarDownload.setProgress((int) (progress.fraction * 100));
                        super.downloadProgress(progress);
                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                //noinspection unchecked
                List<ImageItem> imageItems = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (imageItems != null && imageItems.size() > 0) {
                    ImageItem imageItem = imageItems.get(0);
                    Glide.with(this).load(new File(imageItem.path))//
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)//
                            .into(ivPic);
                    uploadPicToServer(imageItem);
                }
            } else {
                Toast.makeText(this, "没有选择图片", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 上传图片
     *
     * @param imageItem
     */
    private void uploadPicToServer(ImageItem imageItem) {

        NXHttpManager.<String>post("http://app.nt.cn")//
                .tag(this)//
                .params("token", token)
                .params("appid", "1")
                .params("phone_system", "1")
                .params("version_code", "3")
                .params("route", "personal/upload")
                .params("upfile", new File(imageItem.path))
                .execute(new JsonCallback<String>() {

                    @Override
                    public void uploadProgress(Progress progress) {
                        OkLogger.d("progress:" + progress);
                        seekbarUpload.setMax(100);
                        seekbarUpload.setProgress((int) (progress.fraction * 100));
                        tvUploadState.setText("上传状态：上传中");
                        super.uploadProgress(progress);
                    }


                    @Override
                    public void onSuccess(Response<String> response) {
                        tvUploadState.setText("上传状态：上传成功");
                        Log.d(TAG, "onSuccess: ");

                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.d(TAG, "onError: ");
                        tvUploadState.setText("上传状态：上传失败");

                        super.onError(response);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        NXHttpManager.getInstance().cancelTag(this);
        super.onDestroy();
    }
}

