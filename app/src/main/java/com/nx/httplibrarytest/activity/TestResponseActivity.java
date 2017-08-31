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
import com.nx.httplibrary.deprecate.NXResponse;
import com.nx.httplibrary.okhttp.model.Response;
import com.nx.httplibrary.okhttp.utils.FileUtil;
import com.nx.httplibrarytest.AppCallBack;
import com.nx.httplibrarytest.R;

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
                .tag("home.index")
                //.cacheTime(CacheEntity.CACHE_NEVER_EXPIRE) 永久有效
                .execute(new AppCallBack<NXResponse>(this) {

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

        datas.put("home.index", "{ \"data\" : {\"activityInfo\":{\"activityID\":\"1234\",\"bannerUrl\":\"xxxxx\",\"activityRuleUrl\":\"xxxxx\",\"activityState\":0,\"time\":\"2017.8.10-2017-8.05\",\"title\":\"歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 歌名：消愁 \",\"content\":\"当你走进这欢乐场，背上所有的梦与想，各色的脸上各色的妆，没人记得你的模样，三巡酒过你在角落，固执的，唱着苦涩的歌，听他在喧嚣里被淹没，你拿起酒杯对自己说，一杯敬朝阳，一杯敬月光，唤醒我的向往，温柔了寒窗，于是可以不回头的逆风飞翔，不怕心头有雨，眼底有霜，一杯敬故乡，一杯敬远方，守着，的善良，催着我成长，所以南北的路从此不再漫长，灵魂不再无处安放，一杯敬明天，一杯敬过往，支撑我的身体，厚重了肩膀，虽然从不相信所谓山高水长，人生苦短何必念念不忘，一杯敬自由，一杯敬死亡，宽恕我的平凡，驱散了迷惘，好吧天亮之后总是潦草离场，清醒的人最荒唐，好吧天亮之后总是潦草离场，清醒的人最荒唐\"},\"voteInfo\":{\"winer\":\"0\",\"prosNumbers\":\"1600\",\"consNumbers\":\"400\",\"prosPercentage\":\"80%\",\"consPercentage\":\"20%\",\"prosPoint\":\"一杯敬自由,一杯敬死亡！！！\",\"consPoint\":\"反方认为：不喝酒\"},\"totalScore\":\"1000\"},\n" +
                "     \"resCode\":\"0000\",\n" +
                "     \"resMsg\":\"成功\"\n" +
                "}");


        Gson gson = new Gson();
        String jsonData = gson.toJson(datas);

        Log.d(TAG, "testSave: "+Environment.getExternalStorageDirectory().getAbsolutePath() +"/response.txt");
        try {
            FileUtil.writeTxtToFile(jsonData, Environment.getExternalStorageDirectory().getAbsolutePath()+"/","response.txt");

        } catch (Exception e) {
            e.printStackTrace();
            mTvState.setText("结果：写入失败：" + e.toString());
            return ;
        }

        mTvState.setText("结果：写入完成");

//            String json = FileUtil.readTxtFile(new File("/sdcard/response.txt"));


    }
}
