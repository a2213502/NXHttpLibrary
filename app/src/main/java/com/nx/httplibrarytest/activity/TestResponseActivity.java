package com.nx.httplibrarytest.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nx.commonlibrary.BaseActivity.BaseActivity;
import com.nx.commonlibrary.Utils.StringUtil;
import com.nx.httplibrary.NXHttpManager;
import com.nx.httplibrary.deprecate.NXDeprecateCallback;
import com.nx.httplibrary.deprecate.NXResponse;
import com.nx.httplibrary.okhttp.model.Response;
import com.nx.httplibrary.okhttp.utils.FileUtil;
import com.nx.httplibrarytest.R;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.nx.httplibrarytest.R.id.tv_state;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/29 14:44
 */
public class TestResponseActivity extends BaseActivity {
    private TextView mTvState;
    private Button mBtSave;
    private Button mBtTest;

    @Override
    public int getLayoutId() {

        return R.layout.activity_testresponse;

    }

    @Override
    public void initView() {


        mTvState = (TextView) findViewById(tv_state);
        mBtSave = (Button) findViewById(R.id.bt_save);
        mBtTest = (Button) findViewById(R.id.bt_test);

    }

    @Override
    protected void initHeadData() {

    }

    @Override
    protected void initListener() {
        mBtSave.setOnClickListener(this);
        mBtTest.setOnClickListener(this);
    }

    @Override
    protected void initData() {


    }

    @Override
    public void onClickEvent(View view) {

        switch (view.getId()) {
            case R.id.bt_save:

                testSave();
                break;

            case R.id.bt_test:
                testResponse();
                break;
        }
    }

    private void testResponse() {

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
                .tag("testCacheJsonBack")
                //.cacheTime(CacheEntity.CACHE_NEVER_EXPIRE) 永久有效
                .execute(new NXDeprecateCallback<NXResponse>() {

                    @Override
                    public void onSuccess(Response<NXResponse> response) {

                        boolean fromCache = response.isFromCache();


                        NXResponse body = response.body();


                        mTvState.setText(body.toString());
                    }
                });

    }

    private void testSave() {


        //准备数据存储到文件
        Map<String, String> datas = new HashMap<>();

        datas.put("http://ncffront.limingjie.top/index.php", "{ \"data\" : {\"activityInfo\":{\"activityID\":\"xxxx\",\"bannerUrl\":\"xxxxx\",\"activityRuleUrl\":\"xxxxx\",\"activityState\":0,\"time\":\"2017.10.27-2017-11.05\",\"title\":\"xxxxx\",\"content\":\"xxxx\"},\"voteInfo\":{\"winer\":\"1\",\"prosNumbers\":\"1600\",\"consNumbers\":\"400\",\"prosPercentage\":\"80%\",\"consPercentage\":\"20%\",\"prosPoint\":\"xxxxx\",\"consPoint\":\"xxxx\"},\"totalScore\":\"100\"},\n" +
                "     \"resCode\":\"0000\",\n" +
                "     \"resMsg\":\"成功\"\n" +
                "}");

        datas.put("testCacheJsonBack", "{\"data\":{\"username\":\"xxxxxxx\",\"header_img\":\"\",\"nick_name\":\"xxxxxx\",\"token\":\"xxxxxxxx\",\"user_name\":\"185****2015\",\"isselecttopic\":\"1\"},\"resCode\":\"0000\",\"resMsg\":\"成功\"}");

        Gson gson = new Gson();
        String jsonData = gson.toJson(datas);

        try {
            FileUtil.writeTxtFile(jsonData, new File("/sdcard/response.txt"));
        } catch (Exception e) {
            e.printStackTrace();
            mTvState.setText("结果：写入失败：" + e.toString());

        }

        mTvState.setText("结果：写入完成");

//            String json = FileUtil.readTxtFile(new File("/sdcard/response.txt"));


    }
}
