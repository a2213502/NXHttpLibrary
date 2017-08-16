/**
 *
 */
package com.nx.httplibrary.AsyncHttpClient;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nx.commonlibrary.Utils.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhnagqiang
 */
public class Response implements Serializable {

    private String resCode = null;

    private String resMsg = null;

    private Map<String, Object> data = null;

    private String json = null;
    private long createTime = 0L;//创建时间

    public static Response createResponse(String json) {
        Response response = null;
        try {
            if (json == null || "".equals(json)) {
                response = new Response();
                response.setResCode("100000");
                response.setResMsg("返回的数据为空");
            } else {
                Type listType = new TypeToken<Response>() {
                }.getType();
                response = new Gson().fromJson(json, listType);
            }
        } catch (Exception e) {
            Log.e("wang",e.toString());
            Log.e("Response", "返回的数据无法解析:" + json);
            e.printStackTrace();
            response = new Response();
            response.setResCode("100000");
            response.setResMsg("返回的数据不能解析");
        }
        response.createTime = System.currentTimeMillis();
        response.json = json;
        return response;
    }

    /**
     * 服务端是否返回成功
     *
     * @return
     */
    public boolean isSucceed() {
        return ("0000".equals(this.resCode));
    }

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

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    /**
     * 获得data中的一个字符串值
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        if (this.data == null || this.data.containsKey(key) == false) {
            return null;
        }
        return StringUtil.toString(this.data.get(key));
    }

    public Object[] getArgs(String key) {
        if (this.data == null || !this.data.containsKey(key)) {
            return null;
        }
        ArrayList val = (ArrayList) this.data.get(key);
        return val.toArray();
    }

    public Object getObject(String key) {
        if (this.data == null || !this.data.containsKey(key)) {
            return null;
        }
        return this.data.get(key);
    }


    public List<Map<String, String>> getListData(String key) throws ClassCastException {
        if (this.data == null || !this.data.containsKey(key)) {
            return null;
        }
        Object val = this.data.get(key);
        if (val == null) {
            return null;
        }
        if (val instanceof List) {
            return (List<Map<String, String>>) val;
        }
        throw new ClassCastException("获得的数据不是一个List对象");
    }

    public Map<String, Object> getMap(String key) {
        if (this.data.containsKey(key)) {
            return (Map<String, Object>) this.data.get(key);
        }
        return null;
    }

    public long getCreateTime() {
        return createTime;
    }

    public Integer getInteger(String k) {
        if (this.data == null || StringUtil.isEmpty(k) || !this.data.containsKey(k)) {
            return 0;
        }
        Object v = this.data.get(k);
        try {
            if (v instanceof Integer) {
                return (Integer) v;
            } else {
                return Integer.parseInt(String.valueOf(v));
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
