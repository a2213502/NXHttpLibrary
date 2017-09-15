package com.nx.httplibrary.deprecate;

import com.google.gson.Gson;
import com.nx.httplibrary.okhttp.callback.AbsCallback;
import com.nx.httplibrary.okhttp.convert.StringConvert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * @类描述： TODO
 * @创建人：王成丞
 * @创建时间：2017/8/23 15:14
 */
@Deprecated
public abstract class NXDeprecateCallback<T> extends AbsCallback<T> {


    public Class parameterType;
    public Gson mGson;
    private StringConvert convert;

    public NXDeprecateCallback() {
        parameterType = getSuperclassTypeParameter(getClass());
        mGson = new Gson();
        convert = new StringConvert();
    }


    private Class getSuperclassTypeParameter(Class<? extends NXDeprecateCallback> subclass) {


        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            return Object.class;
        }
        //ParameterizedType参数化类型，即泛型
        ParameterizedType p = (ParameterizedType) superclass;
        //获取参数化类型的数组，泛型可能有多个
        return (Class) p.getActualTypeArguments()[0];
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
        //是否是NXResPonse的子类

        if (parameterType == NXResponse.class) {
            data = (T) NXResponse.createResponse(json);

        } else if (parameterType.getSuperclass() == BaseBean.class || parameterType == BaseBean.class) {
            //解析json获取recognizeResult对象
            data = (T) mGson.fromJson(json, parameterType);
        }

        return data;

    }

}
