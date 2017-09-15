package com.nx.httplibrarytest.activity;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nx.commonlibrary.BaseActivity.BaseActivity;
import com.nx.commonlibrary.Utils.StringUtil;
import com.nx.httplibrary.NXHttpManager;
import com.nx.httplibrary.okhttp.model.Response;
import com.nx.httplibrary.okhttp.utils.FileUtil;
import com.nx.httplibrarytest.AppCallBack;
import com.nx.httplibrarytest.R;
import com.nx.httplibrarytest.bean.TestBean;

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

        NXHttpManager.<TestBean>post("http://app.nt.cn")
                .params(params)
                .tag("home.index")
                //.cacheTime(CacheEntity.CACHE_NEVER_EXPIRE) 永久有效
                .execute(new AppCallBack<TestBean>(this) {

                    @Override
                    public void onSuccess(Response<TestBean> response) {

                        boolean fromCache = response.isFromCache();


                        TestBean body = response.body();


                        mTvState.setText(body.toString());
                    }


                });



    }

    private void testSave() {

        //准备数据存储到文件
        Map<String, String> datas = new HashMap<>();

        datas.put("home.index", "{\"data\":{\"activityInfo\":{\"MID\":\"10086\",\"bannerUrl\":\"http://www.nt.cn\",\"activityRuleUrl\":\"http://www.nt.cn\",\"activityState\":\"0\",\"time\":\"2017.10.27-2017.11.05\",\"title\":\"牛谈竞猜活动标题\",\"description\":\"竞猜活动话题简介\",\"relation\":\"0\"},\"voteInfo\":{\"winer\":\"1\",\"prosNumbers\":\"1600\",\"consNumbers\":\"400\",\"prosPercentage\":\"80\",\"consPercentage\":\"20\",\"prosPoint\":\"正方观点\",\"consPoint\":\"反方观点\"},\"userInfo\":{\"pros\":\"0\",\"cons\":\"1\",\"totalScore\":\"100\"}},\"resCode\":\"0000\",\"resMsg\":\"成功\"}");
        datas.put("activity.views", "{\"data\":{\"consult\":[{\"userImg\":\"http://www.nt.cn\",\"nickName\":\"138****8888\",\"viewMid\":\"168\",\"title\":\"活动标题\",\"description\":\"观点描述\",\"likedNum\":\"10086\",\"isPay\":\"0\",\"payIntegral\":\"10086\",\"messageType\":\"2\"}]},\"resCode\":\"0000\",\"resMsg\":\"成功\"}");

        Gson gson = new Gson();
        String jsonData = gson.toJson(datas);

        Log.d(TAG, "testSave: "+Environment.getExternalStorageDirectory().getAbsolutePath() +"/response.txt");
        try {
            FileUtil.writeTxtToFile(jsonData, "/sdcard/","response.txt");

        } catch (Exception e) {
            e.printStackTrace();
            mTvState.setText("结果：写入失败：" + e.toString());
            return ;
        }

        mTvState.setText("结果：写入完成");

//            String json = FileUtil.readTxtFile(new File("/sdcard/response.txt"));


    }
}
