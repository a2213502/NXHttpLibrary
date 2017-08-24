/*
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nx.httplibrary.okhttp.callback;


import com.nx.httplibrary.NXHttpManager;
import com.nx.httplibrary.okhttp.model.Progress;
import com.nx.httplibrary.okhttp.model.Response;
import com.nx.httplibrary.okhttp.request.base.Request;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/8 15:04
 */
public abstract class AbsCallback<T> implements Callback<T> {

    @Override
    public void onStart(Request<T, ? extends Request> request) {
    }

//    @Override
//    public void onCacheSuccess(Response<T> response) {
//    }

    @Override
    public void onError(Response<T> response) {
//        OkLogger.printStackTrace(response.getException());
        int responseCode = response.code();
        Throwable throwable = response.getException();
        //network error
        if (responseCode == 0) {
            showToastMessage("手机网络不可用!");
        } else if (responseCode == 404) {
            showToastMessage("没有找到服务!");
        } else if (responseCode >= 500) {
            showToastMessage("服务器出现错误!" );
        } else if (throwable instanceof ConnectException) {
            showToastMessage("网络连不上服务器!" );
        } else if (throwable instanceof SocketTimeoutException) {
            showToastMessage("网速不给力,超时了,刷新试试!");
        } else if (throwable instanceof UnknownHostException) {
            showToastMessage("手机网络不可用!");

        }

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void uploadProgress(Progress progress) {
    }

    @Override
    public void downloadProgress(Progress progress) {
    }


    public void showToastMessage(String msg) {
        NXHttpManager.getInstance().showToastMessage(msg);
    }
}
