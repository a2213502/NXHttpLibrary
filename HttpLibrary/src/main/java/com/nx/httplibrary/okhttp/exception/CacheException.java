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

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/8 15:04
 */
public class CacheException extends Exception {
    private static final long serialVersionUID = 845628123701073013L;

    public static CacheException NON_OR_EXPIRE(String cacheKey) {
        return new CacheException("cacheKey = " + cacheKey + " ,can't find cache by cacheKey, or cache has expired!");
    }

    public static CacheException NON_AND_304(String cacheKey) {
        return new CacheException("the http response code is 304, but the cache with cacheKey = " + cacheKey + " is null or expired!");
    }

    public CacheException(String detailMessage) {
        super(detailMessage);
    }
}
