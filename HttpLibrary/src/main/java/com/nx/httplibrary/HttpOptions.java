package com.nx.httplibrary;

import com.nx.httplibrary.okhttp.cache.CacheMode;
import com.nx.httplibrary.okhttp.model.HttpHeaders;
import com.nx.httplibrary.okhttp.model.HttpParams;

/**
 * @类描述： Http相关配置类
 * @创建人：王成丞
 * @创建时间：2017/8/8 16:19
 */
public class HttpOptions {


    //通用请求头  header不支持中文，不允许有特殊字符
    public HttpHeaders commonHeaders;
    //是否打印请求log
    public boolean isNeedLog;

    //请求超时时间
    public long connectTimeout;
    //请求重试次数 默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
    public int retryCount;

    public HttpParams commonParams;
    //缓存模式
    public CacheMode cacheMode;

    //缓存时间 -1 为永不过期 毫秒单位

    public long cacheTime = -1;

    public static HttpOptions createDefault() {
        return new Builder().build();
    }

    private HttpOptions() {

    }

    private HttpOptions(final Builder builder) {
        commonHeaders = builder.commonHeaders;
        isNeedLog = builder.isNeedLog;
        connectTimeout = builder.connectTimeout;
        retryCount = builder.retryCount;
        commonParams = builder.commonParams;
        cacheMode = builder.cacheMode;
        cacheTime = builder.cacheTime;
    }


    public static class Builder {
        //通用请求头
        private HttpHeaders commonHeaders = new HttpHeaders();
        //是否打印请求log
        private boolean isNeedLog = true;

        //请求超时时间 单位：毫秒
        private long connectTimeout = 60000;
        //请求重试次数
        private int retryCount = 3;

        //缓存模式 默认不缓存
        public CacheMode cacheMode = CacheMode.REQUEST_FAILED_READ_CACHE;

        //缓存时间 -1 为永不过期 毫秒单位

        public long cacheTime = -1;

        private HttpParams commonParams = new HttpParams();


        public Builder setCacheTime(long cacheTime) {
            this.cacheTime = cacheTime;
            return this;

        }

        public Builder setCacheMode(CacheMode cacheMode) {
            this.cacheMode = cacheMode;
            return this;

        }

        public Builder setCommonHeaders(String headerKey, String headerValue) {
            commonHeaders.put(headerKey, headerValue);
            return this;

        }

        public Builder setCommonParams(String paramKey, String paramValue) {
            commonParams.put(paramKey, paramValue);
            return this;
        }

        public Builder setNeedLog(boolean needLog) {
            isNeedLog = needLog;
            return this;

        }

        public Builder setConnectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;

        }

        public Builder setRetryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;

        }

        public HttpOptions build() {
            return new HttpOptions(this);
        }
    }

}
