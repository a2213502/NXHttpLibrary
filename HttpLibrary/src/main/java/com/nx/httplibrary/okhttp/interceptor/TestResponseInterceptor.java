package com.nx.httplibrary.okhttp.interceptor;

import com.google.gson.Gson;
import com.nx.commonlibrary.Utils.LogUtil;
import com.nx.httplibrary.okhttp.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/29 14:43
 */
public class TestResponseInterceptor implements Interceptor {


    private static final String TAG = "TestResponseInterceptor";
    private String filePath;
    private Map map = null;

    public TestResponseInterceptor(String filePath) {
        this.filePath = filePath;

        try {
            String test = FileUtil.readTxtFile(new File(filePath));
            Gson gson = new Gson();
            map = gson.fromJson(test, Map.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {


        Request request = chain.request();

        Response response = null;

        try {


            response = chain.proceed(request);
            String tag = request.tag().toString();
            LogUtil.d("tag", tag);
            if (map != null && map.containsKey(tag)) {
                String responseJson = (String) map.get(tag);
                return response.newBuilder().body(ResponseBody.create(null, responseJson)).build();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }
}
