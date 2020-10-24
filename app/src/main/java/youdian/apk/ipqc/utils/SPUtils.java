package youdian.apk.ipqc.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;


public class SPUtils {
    private final SharedPreferences sp;

    private static class SPHolder {
        private static final SharedPreferences INSTANCE = PreferenceManager.getDefaultSharedPreferences(Utils.getInstance().getAppContext());
    }

    private SPUtils(@NonNull String name) {
        sp = getSharePreferences(name);
    }

    private SPUtils() {
        sp = getDefaultSharePreferences();
    }

    public static SPUtils newInstance(String name) {
        return new SPUtils(name);
    }

    public static SPUtils getInstance() {
        return new SPUtils();
    }

    private static SharedPreferences getSharePreferences(@NonNull String name) {
        return Utils.getInstance().getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    private static SharedPreferences getDefaultSharePreferences() {
        return SPHolder.INSTANCE;
    }

    private SharedPreferences.Editor getEditor() {
        return sp.edit();
    }

    /**
     * 清空sharePreference保存内容
     *
     * @return
     */
    public SPUtils clear() {
        getEditor().clear().apply();
        return this;
    }

    /**
     * 根据Key删除sharePreference内容
     *
     * @param key
     * @return
     */
    public SPUtils remove(String key) {
        getEditor().remove(key).apply();
        return this;
    }

    /**
     * 保存
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> SPUtils save(String key, T value) {
        SharedPreferences.Editor editor = getEditor();
        if (null == value) {
            editor.remove(key).apply();
        } else if (value instanceof String) {
            editor.putString(key, (String) value).apply();
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value).apply();
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value).apply();
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value).apply();
        }
        return this;
    }

    public <T> T get(String key, T defValue) {
        T value;
        if (defValue instanceof String) {
            String s = sp.getString(key, (String) defValue);
            value = (T) s;
        } else if (defValue instanceof Integer) {
            Integer i = sp.getInt(key, (Integer) defValue);
            value = (T) i;
        } else if (defValue instanceof Long) {
            Long l = sp.getLong(key, (Long) defValue);
            value = (T) l;
        } else if (defValue instanceof Float) {
            Float f = sp.getFloat(key, (Float) defValue);
            value = (T) f;
        } else if (defValue instanceof Boolean) {
            Boolean b = sp.getBoolean(key, (Boolean) defValue);
            value = (T) b;
        } else {
            value = defValue;
        }
        return value;
    }


}
