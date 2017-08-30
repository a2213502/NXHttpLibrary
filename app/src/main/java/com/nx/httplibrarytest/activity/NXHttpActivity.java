package com.nx.httplibrarytest.activity;

import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nx.commonlibrary.BaseActivity.BaseActivity;
import com.nx.commonlibrary.Utils.StringUtil;
import com.nx.httplibrary.NXHttpManager;
import com.nx.httplibrary.deprecate.NXResponse;
import com.nx.httplibrary.okhttp.callback.JsonCallback;
import com.nx.httplibrary.okhttp.callback.StringCallback;
import com.nx.httplibrary.okhttp.model.Response;
import com.nx.httplibrary.okhttp.request.base.Request;
import com.nx.httplibrary.okhttp.utils.OkLogger;
import com.nx.httplibrarytest.AppCallBack;
import com.nx.httplibrarytest.R;
import com.nx.httplibrarytest.bean.LoginBean;

import java.util.HashMap;
import java.util.Map;

/**
 * @类描述： 网络访问Test
 * @创建人：王成丞
 * @创建时间：2017/8/8 18:48
 */
public class NXHttpActivity extends BaseActivity {


    private Button mRequestJson;
    private Button mRequestString;
    private Button mBtCache;
    private TextView mTvResult;
    private String mToken;
    private Button mBtUpload;
    private Button btTestResponse;

    @Override
    public int getLayoutId() {
        return R.layout.activity_http;
    }

    @Override
    public void initView() {
        mRequestJson = (Button) findViewById(R.id.requestJson);
        mRequestString = (Button) findViewById(R.id.requestString);
        mBtCache = (Button) findViewById(R.id.bt_cache);
        mBtUpload = (Button) findViewById(R.id.bt_upload);
        btTestResponse = (Button) findViewById(R.id.bt_testresponse);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mTvResult.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    @Override
    protected void initHeadData() {

    }

    @Override
    protected void initListener() {
        mRequestJson.setOnClickListener(this);
        mRequestString.setOnClickListener(this);
        mBtCache.setOnClickListener(this);
        btTestResponse.setOnClickListener(this);
        mBtUpload.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClickEvent(View view) {

        mTvResult.setText("");
        switch (view.getId()) {
            case R.id.requestJson:

                testJsonBack();
                break;
            case R.id.requestString:

                testStringBack();
                break;
            case R.id.bt_cache:

                testCacheJsonBack();
                break;
            case R.id.bt_upload:

                startActivity(UpLoadActivity.class);
                break;
            case R.id.bt_testresponse:

                Intent intent = new Intent(this, TestResponseActivity.class);
                startActivity(intent);
                break ;
        }
    }

    /**
     * 跳转Acitivity
     *
     * @param clazz target activity
     */
    private void startActivity(Class clazz) {
        if (mToken == null) {
            Toast.makeText(this, "先获取图片上传需要的Token", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 缓存服务器返回的json数据
     */
    private void testCacheJsonBack() {

        Map<String, String> params = new HashMap<>();
        params.put("token", "");
//        params.put("mobile", "18514592015");
        params.put("password", StringUtil.MD5(StringUtil.MD5("a123456")));
        params.put("route", "user/login");
        params.put("thirdPartToken", "");
        params.put("type", "");
        //注意javabean须实现 Serializable

        NXHttpManager.<NXResponse>post("http://app.nt.cn")
                .params(params)
                //如果网络请求失败使用缓存数据
//                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                //毫秒
//                .cacheTime(-1)
                .tag("testCacheJsonBack")
                //.cacheTime(CacheEntity.CACHE_NEVER_EXPIRE) 永久有效
                .execute(new AppCallBack(NXHttpActivity.this) {


                    @Override
                    public void onSuccess(Response<NXResponse> response) {

                        boolean fromCache = response.isFromCache();


                        NXResponse body = response.body();

                        mToken = body.getString("token");
                        mTvResult.setText(body.toString());
                    }
                });


    }

    /**
     * 服务器返回Json转换为String对象
     */
    private void testStringBack() {

        NXHttpManager.<String>post("http://app.nt.cn")
                .params("token", "")
                .params("mobile", "18514592015")
                .params("password", StringUtil.MD5(StringUtil.MD5("a123456")))
                .params("route", "user/login")
                .params("thirdPartToken", "")
                .params("type", "")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkLogger.d(response.body().toString());
                        mTvResult.setText(response.body().toString());
                    }
                });
    }


    /**
     * 服务器返回json，并转换成javabean的调用示范
     */
    private void testJsonBack() {

        NXHttpManager.<LoginBean>post("http://app.nt.cn")
                .params("token", "")
                .params("mobile", "18514592015")
                .params("password", StringUtil.MD5(StringUtil.MD5("a123456")))
                .params("route", "user/login")
                .params("thirdPartToken", "")
                .params("type", "")
                .tag(this)
                .execute(new JsonCallback<LoginBean>() {


                    @Override
                    public void onStart(Request<LoginBean, ? extends Request> request) {

                        OkLogger.d("onStart:");

                        super.onStart(request);
                    }

//                    @Override
//                    public void onCacheSuccess(Response<LoginBean> response) {
//                        OkLogger.d("onCacheSuccess:" + response.body().toString());
//                        mTvResult.setText(response.body().toString());
//
//                        super.onCacheSuccess(response);
//                    }

                    @Override
                    public void onSuccess(Response<LoginBean> response) {
                        OkLogger.d("onSuccess:" + response.body().toString());
                        mTvResult.setText(response.body().toString());
                    }

                    @Override
                    public void onError(Response<LoginBean> response) {
                        OkLogger.d("onError:");
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        OkLogger.d("onFinish:");
                        super.onFinish();
                    }
                });
    }

    @Override
    protected void onDestroy() {

        NXHttpManager.getInstance().cancelTag(this);
        super.onDestroy();
    }
}
