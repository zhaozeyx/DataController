package com.demon.library.preference

/*
 * 文件名: CustomPreference
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/3/26
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */

import android.content.SharedPreferences

class CustomPreference(private val preferenceProxy: SharedPreferences) : IPreferences {

    private var mEditor: SharedPreferences.Editor? = null

    private val editor: SharedPreferences.Editor
        get() {
            if (null == mEditor) {
                mEditor = edit()
            }
            return mEditor as SharedPreferences.Editor
        }

    override fun getAll(): Map<String, *> {
        return preferenceProxy.all
    }

    override fun getString(key: String, defValue: String?): String? {
        return preferenceProxy.getString(key, defValue)
    }

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? {
        return preferenceProxy.getStringSet(key, defValues)
    }

    override fun getInt(key: String, defValue: Int): Int {
        return preferenceProxy.getInt(key, defValue)
    }

    override fun getLong(key: String, defValue: Long): Long {
        return preferenceProxy.getLong(key, defValue)
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return preferenceProxy.getFloat(key, defValue)
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return preferenceProxy.getBoolean(key, defValue)
    }

    override fun contains(key: String): Boolean {
        return preferenceProxy.contains(key)
    }

    override fun edit(): SharedPreferences.Editor {
        return preferenceProxy.edit()
    }

    override fun putBoolean(key: String, `val`: Boolean): SharedPreferences.Editor? {
        editor.putBoolean(key, `val`)
        return mEditor
    }

    override fun putInteger(key: String, `val`: Int): SharedPreferences.Editor? {
        editor.putInt(key, `val`)
        return mEditor
    }

    override fun putLong(key: String, `val`: Long): SharedPreferences.Editor? {
        editor.putLong(key, `val`)
        return mEditor
    }

    override fun putFloat(key: String, `val`: Float): SharedPreferences.Editor? {
        editor.putFloat(key, `val`)
        return mEditor
    }

    override fun putString(key: String, `val`: String): SharedPreferences.Editor? {
        editor.putString(key, `val`)
        return mEditor
    }

    override fun put(vals: Map<String, *>): SharedPreferences.Editor? {
        //        return getEditor().;
        return mEditor
    }

    override fun commit() {
        if (null == mEditor) {
            return
        }
        mEditor?.apply()
    }

    override fun remove(key: String?): SharedPreferences.Editor {
        editor.remove(key)
        return editor
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {

    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {

    }
}
