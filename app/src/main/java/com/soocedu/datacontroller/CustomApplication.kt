package com.soocedu.datacontroller

import android.app.Application
import android.content.Context
import com.demon.library.preference.CustomPreference
import com.demon.library.preference.IPreferences
import com.soocedu.datacontroller.constant.PreferenceKey
import timber.log.Timber

/*
 * 文件名: CustomApplication
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        preference = CustomPreference(getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE))
        Timber.plant(Timber.DebugTree())
        initConst()
    }

    private fun initConst() {
        preference.putString(PreferenceKey.KEY_SERVER_URL, BuildConfig.SERVER_URL).apply()
    }

    companion object {
        const val PREFERENCE_NAME = "sooc_preference"
        lateinit var preference: IPreferences
    }
}