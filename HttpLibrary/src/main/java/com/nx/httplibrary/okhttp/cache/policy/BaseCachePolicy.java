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

import android.graphics.Bitmap;

import com.nx.httplibrary.NXHttpManager;
import com.nx.httplibrary.okhttp.cache.CacheEntity;
import com.nx.httplibrary.okhttp.cache.CacheMode;
import com.nx.httplibrary.okhttp.callback.Callback;
import com.nx.httplibrary.okhttp.db.CacheManager;
import com.nx.httplibrary.okhttp.exception.HttpException;
import com.nx.httplibrary.okhttp.model.Response;
import com.nx.httplibrary.okhttp.request.base.Request;
import com.nx.httplibrary.okhttp.utils.HeaderParser;
import com.nx.httplibrary.okhttp.utils.HttpUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;
import okhttp3.Headers;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/8 15:04
 */
public abstract class BaseCachePolicy<T> implements CachePolicy<T> {

    protected Request<T, ? extends Request> request;
    protected volatile boolean canceled;
    protected volatile int currentRetryCount = 0;
    protected boolean executed;
    protected Call rawCall;
    protected Callback<T> mCallback;
    protected CacheEntity<T> cacheEntity;

    public BaseCachePolicy(Request<T, ? extends Request> request) {
        this.request = request;
    }

    @Override
    public boolean onAnalysisResponse(Call call, okhttp3.Response response) {
        return false;
    }

    @Override
    public CacheEntity<T> prepareCache() {
        //check the config of cache
        if (request.getCacheKey() == null) {
            request.cacheKey(HttpUtils.createUrlFromParams(request.getBaseUrl(), request.getParams().urlParamsMap));
        }
        if (request.getCacheMode() == null) {
            request.cacheMode(CacheMode.NO_CACHE);
        }

        CacheMode cacheMode = request.getCacheMode();
        if (cacheMode != CacheMode.NO_CACHE) {
            //noinspection unchecked
            cacheEntity = (CacheEntity<T>) CacheManager.getInstance().get(request.getCacheKey());
            HeaderParser.addCacheHeaders(request, cacheEntity, cacheMode);
            if (cacheEntity != null && cacheEntity.checkExpire(cacheMode, request.getCacheTime(), System.currentTimeMillis())) {
                cacheEntity.setExpire(true);
            }
        }

        if (cacheEntity == null || cacheEntity.isExpire() || cacheEntity.getData() == null || cacheEntity.getResponseHeaders() == null) {
            cacheEntity = null;
        }
        return cacheEntity;
    }

    @Override
    public synchronized Call prepareRawCall() throws Throwable {
        if (executed) throw new RuntimeException("Already executed!");
        executed = true;
        rawCall = request.getRawCall();
        if (canceled) rawCall.cancel();
        return rawCall;
    }

    protected Response<T> requestNetworkSync() {
        try {
            okhttp3.Response response = rawCall.execute();
            int responseCode = response.code();

            //network error
            if (responseCode == 404) {
                return Response.error(false, HttpException.RESPONSE_CODE_404());
            } else if (responseCode >= 500) {
                return Response.error(false, HttpException.RESPONSE_CODE_500());
            }

            T body = request.getConverter().convertResponse(response);
            //save cache when request is successful

            if (body == null) {
                return Response.error(false, HttpException.BODY_NULL_ERROR());
            }
            saveCache(response.headers(), body);
            return Response.success(false, body);
        } catch (Throwable throwable) {
            if (throwable instanceof SocketTimeoutException && currentRetryCount < request.getRetryCount()) {
                currentRetryCount++;
                rawCall = request.getRawCall();
                if (canceled) {
                    rawCall.cancel();
                } else {
                    requestNetworkSync();
                }
            }
            return Response.error(false, HttpException.OTHER_ERROR(throwable));
        }
    }

    protected void requestNetworkAsync() {
        rawCall.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException && currentRetryCount < request.getRetryCount()) {
                    //retry when timeout
                    currentRetryCount++;
                    rawCall = request.getRawCall();
                    if (canceled) {
                        rawCall.cancel();
                    } else {
                        rawCall.enqueue(this);
                    }
                } else {
                    if (!call.isCanceled()) {
                        Response<T> error = Response.error(false, HttpException.OTHER_ERROR(e));
                        onError(error);
                    }
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                int responseCode = response.code();

                //network error
                if (responseCode == 404) {
                    Response<T> error = Response.error(false, HttpException.RESPONSE_CODE_404());
                    onError(error);
                    return;
                } else if (responseCode >= 500) {
                    Response<T> error = Response.error(false, HttpException.RESPONSE_CODE_500());
                    onError(error);
                    return;
                }

                if (onAnalysisResponse(call, response)) return;

                try {
                    T body = request.getConverter().convertResponse(response);

                    if (body == null) {
                        Response<T> error = Response.error(false, HttpException.BODY_NULL_ERROR());
                        onError(error);
                        return;
                    }
                    //save cache when request is successful
                    saveCache(response.headers(), body);
                    Response<T> success = Response.success(false, body);
                    onSuccess(success);
                } catch (Throwable throwable) {
                    Response<T> error = Response.error(false, HttpException.OTHER_ERROR(throwable));
                    onError(error);
                }
            }
        });
    }

    /**
     * 请求成功后根据缓存模式，更新缓存数据
     *
     * @param headers 响应头
     * @param data    响应数据
     */
    private void saveCache(Headers headers, T data) {
        if (request.getCacheMode() == CacheMode.NO_CACHE) return;    //不需要缓存,直接返回
        if (data instanceof Bitmap) return;             //Bitmap没有实现Serializable,不能缓存

        CacheEntity<T> cache = HeaderParser.createCacheEntity(headers, data, request.getCacheMode(), request.getCacheKey());
        if (cache == null) {
            //服务器不需要缓存，移除本地缓存
            CacheManager.getInstance().remove(request.getCacheKey());
        } else {
            //缓存命中，更新缓存
            CacheManager.getInstance().replace(request.getCacheKey(), cache);
        }
    }

    protected void runOnUiThread(Runnable run) {
        NXHttpManager.getInstance().getDelivery().post(run);
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public void cancel() {
        canceled = true;
        if (rawCall != null) {
            rawCall.cancel();
        }
    }

    @Override
    public boolean isCanceled() {
        if (canceled) return true;
        synchronized (this) {
            return rawCall != null && rawCall.isCanceled();
        }
    }
}
