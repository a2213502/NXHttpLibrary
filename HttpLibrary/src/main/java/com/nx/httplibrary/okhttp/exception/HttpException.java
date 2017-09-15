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
package com.nx.httplibrary.okhttp.exception;


import static com.nx.httplibrary.okhttp.exception.HttpException.ERRORTYPE.BODY_NULL_ERROR;
import static com.nx.httplibrary.okhttp.exception.HttpException.ERRORTYPE.NET_CONNECT_ERROR;
import static com.nx.httplibrary.okhttp.exception.HttpException.ERRORTYPE.NET_SOCKET_TIMEOUT;
import static com.nx.httplibrary.okhttp.exception.HttpException.ERRORTYPE.OTHER_ERROR;
import static com.nx.httplibrary.okhttp.exception.HttpException.ERRORTYPE.RESPONSE_CODE_404;
import static com.nx.httplibrary.okhttp.exception.HttpException.ERRORTYPE.RESPONSE_CODE_500;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/8 15:04
 */
public class HttpException {


    public enum ERRORTYPE {

        NET_CONNECT_ERROR,
        NET_SOCKET_TIMEOUT,
        RESPONSE_CODE_500,
        RESPONSE_CODE_404,
        BODY_NULL_ERROR,
        OTHER_ERROR,
        CACHE_NULL_ERROR
    }

    //错误类型
    private ERRORTYPE errorType;
    //文字描述
    private String message;                         //HTTP status message

    private Throwable throwable;


    public HttpException(ERRORTYPE errorType, Throwable throwable) {
        this.errorType = errorType;
        this.throwable = throwable;
    }

    public HttpException(ERRORTYPE errorType, String message) {
        this.errorType = errorType;
        this.message = message;
    }

    public static HttpException RESPONSE_CODE_500() {
        return new HttpException(RESPONSE_CODE_500, "服务器内部错误!");
    }

    public static HttpException NET_SOCKET_TIMEOUT() {
        return new HttpException(NET_SOCKET_TIMEOUT, "网络传输失败，请检查网络!");
    }

    public static HttpException NET_CONNECT_ERROR() {

        return new HttpException(NET_CONNECT_ERROR, "网络连接失败，请检查网络!");
    }

    public static HttpException RESPONSE_CODE_404() {
        return new HttpException(BODY_NULL_ERROR, "网络访问地址有误!");
    }

    public static HttpException BODY_NULL_ERROR() {
        return new HttpException(RESPONSE_CODE_404, "服务器返回数据错误!");
    }

    public static HttpException OTHER_ERROR(Throwable e) {
        return new HttpException(OTHER_ERROR, e);
    }


    public static HttpException CACHE_NULL_ERROR() {
        return new HttpException(OTHER_ERROR, "缓存数据异常！");
    }

    public ERRORTYPE getErrorType() {
        return errorType;
    }


    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
