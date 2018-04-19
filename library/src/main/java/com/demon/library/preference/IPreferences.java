package com.demon.library.preference;
/*
 * 文件名: IPreferences
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/3/26
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */

import android.content.SharedPreferences;

import java.util.Map;

public interface IPreferences extends SharedPreferences {
    SharedPreferences.Editor putBoolean(String key, boolean val);

    SharedPreferences.Editor putInteger(String key, int val);

    SharedPreferences.Editor putLong(String key, long val);

    SharedPreferences.Editor putFloat(String key, float val);

    SharedPreferences.Editor putString(String key, String val);

    SharedPreferences.Editor put(Map<String, ?> vals);

    SharedPreferences.Editor remove(String key);

    void commit();
}
