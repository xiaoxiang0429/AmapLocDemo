package com.tz.amaplocdemo.util;

import android.content.Context;
import android.content.SharedPreferences;


import java.util.List;

/**
 * @author Tian
 * @date 2019/7/14.
 * email：piner1029@gmail.com
 * description：
 */
public class SPUtils {
    private static final String LIST_TAG = ".LIST";
    private static SharedPreferences sharedPreferences;

    /**
     * 使用之前初始化, 可在Application中调用
     * @param context 请传入ApplicationContext避免内存泄漏
     */
    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("shared_files",
                Context.MODE_PRIVATE);
    }

    private static void checkInit() {
        if (sharedPreferences == null) {
            throw new IllegalStateException("Please call init(context) first.");
        }
    }

    /**
     * 保存对象数据至SharedPreferences, key默认为类名, 如
     * <pre>
     * PreferencesHelper.putData(saveUser);
     * </pre>
     * @param data 不带泛型的任意数据类型实例
     */
    public static <T> void putData(T data) {
        putData(data.getClass().getName(), data);
    }

    /**
     * 根据key保存对象数据至SharedPreferences, 如
     * <pre>
     * PreferencesHelper.putData(key, saveUser);
     * </pre>
     * @param data 不带泛型的任意数据类型实例
     */
    public static <T> void putData(String key, T data) {
        checkInit();
        if (data == null)
            throw new IllegalStateException("data should not be null.");
        sharedPreferences.edit().putString(key, GsonUtils.obj2Json(data)).apply();
    }

    /**
     * 保存List集合数据至SharedPreferences, 请确保List至少含有一个元素, 如
     * <pre>
     * PreferencesHelper.putData(users);
     * </pre>
     * @param data List类型实例
     */
    public static <T> void putData(List<T> data) {
        checkInit();
        if (data == null || data.size() <= 0)
            throw new IllegalStateException(
                    "List should not be null or at least contains one element.");
        Class returnType = data.get(0).getClass();
        sharedPreferences.edit().putString(returnType.getName() + LIST_TAG,
                GsonUtils.obj2Json(data)).apply();
    }

    /**
     * 将数据从SharedPreferences中取出, key默认为类名, 如
     * <pre>
     * User user = PreferencesHelper.getData(key, User.class)
     * </pre>
     */
    public static <T> T getData(Class<T> clz) {
        return getData(clz.getName(), clz);
    }

    /**
     * 根据key将数据从SharedPreferences中取出, 如
     * <pre>
     * User user = PreferencesHelper.getData(User.class)
     * </pre>
     */
    public static <T> T getData(String key, Class<T> clz) {
        checkInit();
        String json = sharedPreferences.getString(key, "");
        return GsonUtils.json2Bean(json,clz);
    }

    /**
     * 将数据从SharedPreferences中取出, 如
     * <pre>List<User> users = PreferencesHelper.getData(List.class, User.class)</pre>
     */
    public static <T> List<T> getData(Class<List> clz, Class<T> gClz) {
        checkInit();
        String json = sharedPreferences.getString(gClz.getName() + LIST_TAG, "[]");
        return GsonUtils.jsonToList(json,gClz);
    }

    /**
     * 简易字符串保存, 仅支持字符串
     */
    public static void putString(String key, String data) {
        sharedPreferences.edit().putString(key, data).apply();
    }

    /**
     * 简易字符串获取, 仅支持字符串
     */
    public static String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public static void putInt(String key, int data) {
        sharedPreferences.edit().putInt(key,data).apply();
    }

    public static int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public static void putBoolean(String key, boolean data) {
        sharedPreferences.edit().putBoolean(key,data).apply();
    }

    public static boolean getBoolean(String key, boolean defaultData) {
        return sharedPreferences.getBoolean(key, defaultData);
    }

    public static void putFloat(String key, float data) {
        sharedPreferences.edit().putFloat(key,data).apply();
    }

    public static float getFloat(String key, float defaultData) {
        return sharedPreferences.getFloat(key, defaultData);
    }

    public static void putLong(String key, long data) {
        sharedPreferences.edit().putLong(key,data).apply();
    }

    public static float getLong(String key, long defaultData) {
        return sharedPreferences.getLong(key, defaultData);
    }

    public static void clear() {
        sharedPreferences.edit().clear().apply();
    }

    /**
     * 删除保存的对象
     */
    public static void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    /**
     * 删除保存的对象
     */
    public static void remove(Class clz) {
        remove(clz.getName());
    }

    /**
     * 删除保存的数组
     */
    public static void removeList(Class clz) {
        sharedPreferences.edit().remove(clz.getName() + LIST_TAG).apply();
    }

}
