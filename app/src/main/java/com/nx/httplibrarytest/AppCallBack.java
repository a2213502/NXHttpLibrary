package com.nx.httplibrarytest;

import android.app.Activity;
import android.app.AlertDialog;

import com.nx.httplibrary.deprecate.NXDeprecateCallback;
import com.nx.httplibrary.deprecate.NXResponse;
import com.nx.httplibrary.okhttp.request.base.Request;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/23 10:41
 */
public abstract class AppCallBack extends NXDeprecateCallback<NXResponse> {


    private AlertDialog dialog;
    private Activity activity;



    public AppCallBack(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onStart(Request request) {

        dialog = new AlertDialog.Builder(activity).create();
        dialog.setMessage("正在加载。。。");
        dialog.show();

        super.onStart(request);
    }



    @Override
    public void onFinish() {
        dialog.dismiss();
        super.onFinish();
    }
}
