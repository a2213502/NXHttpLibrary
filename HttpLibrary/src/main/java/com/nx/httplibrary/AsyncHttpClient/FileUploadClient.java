package com.nx.httplibrary.AsyncHttpClient;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhnagqiang on 15/4/11.
 */
public class FileUploadClient {

    private static String TAG = "FileUploadClient";
    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * 压缩图片后上传
     *
     * @param url
     * @param params
     * @param responseHandler
     * @param path
     */
    public static void uploadCompressImage(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, String... path) {
        if (path == null || path.length == 0) {
            Log.w(TAG, "没有要上传的文件");
            return;
        }

        for (int i = 0; i < path.length; i++) {
            try {
                File file = new File(path[i]);
                //对文件进行压缩处理
                ABImageProcess.compressImage2SD(file, path[i], 712, 960, 85);
                params.put("profile_picture" + i, new File(path[i]));
            } catch (Exception e) {
                Log.e(TAG, "没有找到文件", e);
            }
        }
        client.post(url, params, responseHandler);
    }


    /**
     * 上传文件
     *
     * @param context
     * @param url
     * @param params
     * @param responseHandler
     * @param uris
     */
    public static void uploadFile(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler, Uri... uris) {
        if (uris == null || uris.length == 0) {
            Log.w(TAG, "没有要上传的文件");
            return;
        }
        for (int i = 0; i < uris.length; i++) {
            try {
                params.put("profile_picture" + i, ABFileUtil.uri2File(context, uris[i]));
            } catch (Exception e) {
                Log.e(TAG, "没有找到文件", e);
            }
        }
        client.post(url, params, responseHandler);
    }

    /**
     * 压缩图片后上传
     *
     * @param url
     * @param params
     * @param responseHandler
     * @param path
     */
    public static void uploadFile(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, String... path) {
        if (path == null || path.length == 0) {
            Log.w(TAG, "没有要上传的文件");
            return;
        }
        for (int i = 0; i < path.length; i++) {
            try {
                File file = new File(path[i]);
                params.put("profile_picture" + i, new File(path[i]));
            } catch (Exception e) {
                Log.e(TAG, "没有找到文件", e);
            }
        }
        client.post(url, params, responseHandler);
    }


    public static void uploadFile(final String url, final RequestParams params, final boolean isZoom, final FastHttpResponseHandler responseHandler, final String... path) {
        if (path == null || path.length == 0) {
            Log.w(TAG, "没有要上传的文件");
            return;
        }
        final List<File> tempList = new ArrayList<File>();//临时文件
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute() {
                    if (responseHandler != null) {
                        responseHandler.onStart();
                    }
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    for (int i = 0; i < path.length; i++) {
                        File file = null;
                        try {
                            //对文件进行压缩处理
                            if (isZoom) {
                                file = new File(path[i] + ".tmp");
                                ABImageProcess.compressImage2SD(file, path[i], 712, 960, 85);
                                params.put("profile_picture" + i, file);
                                tempList.add(file);
                            } else {
                                params.put("profile_picture" + i, new File(path[i]));
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "没有找到文件", e);
                            continue;
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    client.post(url, params, new FastHttpResponseHandler() {
                        @Override
                        public void onResponse(Response response) {
                            deleteTemp(tempList);//删除零时文件
                            if (responseHandler != null) {
                                responseHandler.onResponse(response);

                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            deleteTemp(tempList);//删除零时文件
                            if (responseHandler != null) {
                                responseHandler.onFailure(i, headers, bytes, throwable);
                            }
                        }

                        @Override
                        public void onCancel() {
                            deleteTemp(tempList);
                        }

                        @Override
                        public void onFinish() {

                            if (responseHandler != null) {
                                responseHandler.onFinish();
                            }
                        }
                    });
                }
            }.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteTemp(List<File> list) {
        if (list != null) {
            for (File file : list) {
                try {
                    file.delete();
                } catch (Exception e) {
                    continue;
                }
            }
            list = null;//释放缓存
        }
    }
}
