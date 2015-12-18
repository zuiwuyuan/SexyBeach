package com.lnyp.sexybeach;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class FastJsonUtil {

    /**
     * 将对象转化为字符串
     *
     * @param obj
     * @return
     */
    public String Object2Json2(Object obj)
    {
        return JSON.toJSONString(obj);
    }

    /**
     * 将对象转化为字符串(泛型实现)
     *
     * @return
     */
    public static <T> String t2Json2(T t)
    {
        return JSON.toJSONString(t);
    }

    /**
     * 将字符转化为对象
     *
     * @param <T>
     * @param jsonString
     * @param clazz
     * @return
     */
    public static <T> T json2T(String jsonString, Class<T> clazz)
    {
        return JSON.parseObject(jsonString, clazz);
    }

    /**
     * 将字符串数组转化为对象集合
     *
     * @param <T>
     * @param jsonStr
     * @param clazz
     * @return
     */
    public static <T> List<T> json2Collection(String jsonStr, Class<T> clazz)
    {
        return JSON.parseArray(jsonStr, clazz);
    }
}
