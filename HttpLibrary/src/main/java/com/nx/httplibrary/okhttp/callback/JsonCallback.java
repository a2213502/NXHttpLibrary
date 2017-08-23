
package com.nx.httplibrary.okhttp.callback;


import com.google.gson.Gson;
import com.nx.httplibrary.okhttp.convert.StringConvert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * @类描述： 默认将返回的数据解析成需要的Bean, 可以是 BaseBean，String，List，Map
 * @创建人：王成丞
 * @创建时间：2017/8/8 15:04
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    public Type parameterType;
    public Gson mGson;
    private StringConvert convert;

    public JsonCallback() {
        parameterType = getSuperclassTypeParameter(getClass());
        mGson = new Gson();
        convert = new StringConvert();
    }


    private Type getSuperclassTypeParameter(Class<? extends JsonCallback> subclass) {


        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            return Object.class;
        }
        //ParameterizedType参数化类型，即泛型
        ParameterizedType p = (ParameterizedType) superclass;
        //获取参数化类型的数组，泛型可能有多个
        return p.getActualTypeArguments()[0];
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        String json = convert.convertResponse(response);


        T data = null;
        if (parameterType == String.class) {
            data = (T) json;
        } else {
            //解析json获取recognizeResult对象
            data = (T) mGson.fromJson(json, parameterType);

        }
        return data;

    }
}