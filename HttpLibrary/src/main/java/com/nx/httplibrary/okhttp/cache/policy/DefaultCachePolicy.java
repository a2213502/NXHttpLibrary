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
package com.nx.httplibrary.okhttp.cache.policy;


import com.nx.httplibrary.okhttp.cache.CacheEntity;
import com.nx.httplibrary.okhttp.callback.Callback;
import com.nx.httplibrary.okhttp.exception.HttpException;
import com.nx.httplibrary.okhttp.model.Response;
import com.nx.httplibrary.okhttp.request.base.Request;

import okhttp3.Call;

import static com.nx.httplibrary.okhttp.model.Response.error;


/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/8 15:04
 */
public class DefaultCachePolicy<T> extends BaseCachePolicy<T> {

    public DefaultCachePolicy(Request<T, ? extends Request> request) {
        super(request);
    }

    @Override
    public void onSuccess(final Response<T> success) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(success);
                mCallback.onFinish();
            }
        });
    }

    @Override
    public void onError(final Response<T> error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onError(error);
                mCallback.onFinish();
            }
        });
    }

    @Override
    public boolean onAnalysisResponse(final Call call, final okhttp3.Response response) {
        if (response.code() != 304) return false;

        if (cacheEntity == null) {
            final Response<T> error = error(true, HttpException.CACHE_NULL_ERROR());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallback.onError(error);
                    mCallback.onFinish();
                }
            });
        } else {
            final Response<T> success = Response.success(true, cacheEntity.getData());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCallback.onSuccess(success);
                    mCallback.onFinish();
                }
            });
        }
        return true;
    }

    @Override
    public Response<T> requestSync(CacheEntity<T> cacheEntity) {
        try {
            prepareRawCall();
        } catch (Throwable throwable) {
            return Response.error(false, HttpException.OTHER_ERROR(throwable));
        }
        Response<T> response = requestNetworkSync();
        //HTTP cache protocol
        if (response.isSuccessful()) {
            if (cacheEntity == null) {
                response = error(true, HttpException.CACHE_NULL_ERROR());
            } else {
                response = Response.success(true, cacheEntity.getData());
            }
        }
        return response;
    }

    @Override
    public void requestAsync(CacheEntity<T> cacheEntity, Callback<T> callback) {
        mCallback = callback;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCallback.onStart(request);

                try {
                    prepareRawCall();
                } catch (Throwable throwable) {
                    Response<T> error = Response.error(false, HttpException.OTHER_ERROR(throwable));
                    mCallback.onError(error);
                    return;
                }
                requestNetworkAsync();
            }
        });
    }
}
