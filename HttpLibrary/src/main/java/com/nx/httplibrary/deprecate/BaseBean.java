package com.nx.httplibrary.deprecate;

import java.io.Serializable;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/28 16:35
 */
public class BaseBean implements Serializable {


    public String resCode;
    public String resMsg;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }
}
