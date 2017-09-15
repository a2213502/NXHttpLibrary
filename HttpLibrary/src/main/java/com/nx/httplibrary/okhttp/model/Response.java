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
package com.nx.httplibrary.okhttp.model;

import com.nx.httplibrary.okhttp.exception.HttpException;

/**
 * @类描述： 响应体的包装类
 * @创建人：王成丞
 * @创建时间：2017/8/8 15:04
 */
public final class Response<T> {

    private T body;
    private HttpException exception;
    private boolean isFromCache;

    public static <T> Response<T> success(boolean isFromCache, T body) {
        Response<T> response = new Response<>();
        response.setFromCache(isFromCache);
        response.setBody(body);
        return response;
    }

    public static <T> Response<T> error(boolean isFromCache, HttpException exception) {
        Response<T> response = new Response<>();
        response.setFromCache(isFromCache);
        response.setException(exception);
        return response;
    }

    public Response() {
    }


    public boolean isSuccessful() {
        return exception == null;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public T body() {
        return body;
    }

    public HttpException getException() {
        return exception;
    }

    public void setException(HttpException exception) {
        this.exception = exception;
    }


    public boolean isFromCache() {
        return isFromCache;
    }

    public void setFromCache(boolean fromCache) {
        isFromCache = fromCache;
    }
}
