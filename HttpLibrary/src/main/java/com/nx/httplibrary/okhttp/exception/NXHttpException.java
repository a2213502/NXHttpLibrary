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
public class NXHttpException extends Exception {
    private static final long serialVersionUID = -8641198158155821498L;

    public NXHttpException(String detailMessage) {
        super(detailMessage);
    }

    public static NXHttpException UNKNOWN() {
        return new NXHttpException("unknown exception!");
    }

    public static NXHttpException BREAKPOINT_NOT_EXIST() {
        return new NXHttpException("breakpoint file does not exist!");
    }

    public static NXHttpException BREAKPOINT_EXPIRED() {
        return new NXHttpException("breakpoint file has expired!");
    }
}
