package com.nx.httplibrary.AsyncHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nx.commonlibrary.BaseView.IBaseView;

import org.apache.http.Header;
import org.apache.http.conn.HttpHostConnectException;

import java.net.SocketTimeoutException;

/**
 * Created by zhnagqiang on 15/4/7.
 */
public abstract class FastHttpResponseHandler extends AsyncHttpResponseHandler {

    private final static String TAG = "FastHttpResponseHandler";
    private IBaseView view = null;
    private Activity context;
    private ProgressDialog dialog;

    public FastHttpResponseHandler() {
    }

    public FastHttpResponseHandler(IBaseView view) {
        this.view = view;
    }

    public FastHttpResponseHandler(IBaseView view, Activity context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.view != null && this.context != null) {
            dialog = new ProgressDialog(context);
            dialog.show();
        } else if (this.view != null) {
            this.view.showLoadingDialog("正在加载...");
        }
    }

        @Override
    public void onSuccess(int i, Header[] headers, byte[] bytes) {
        String data = new String(bytes);
        Log.d(TAG, data);
        try {
            Response response = Response.createResponse(data);
            if (response != null) {
                Log.w("hanshuai", "数据返回====" + response.getJson());
            }
            this.onResponse(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgress(int bytesWritten, int totalSize) {
        super.onProgress(bytesWritten, totalSize);
        if (this.view != null) {
            this.view.onProgress(bytesWritten, totalSize);
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        if (this.view != null && context != null) {
            dialog.dismiss();
        }
        if (this.view != null) {
            this.view.cancelLoadingDialog();
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
        Log.e(TAG, "异常类型:" + throwable.getClass() + throwable.getMessage());

        if (this.view == null)
            return;
        if (throwable instanceof HttpHostConnectException) {
            this.view.showToastMessage("网络连不上服务器:" + i);
        } else if (throwable instanceof SocketTimeoutException) {
            this.view.showToastMessage("网速不给力,超时了,刷新试试!" + i);
        } else if (i == 0) {
            this.view.showToastMessage("手机网络不可用:" + i);
        } else if (i == 404) {
            this.view.showToastMessage("没有找到服务:" + i);
        } else if (this.view != null) {
            this.view.showToastMessage("您的手机网络有点问题 ~ " + i);
        }


    }

    public abstract void onResponse(Response response);

    public void setView(IBaseView view) {
        this.view = view;
    }
}
