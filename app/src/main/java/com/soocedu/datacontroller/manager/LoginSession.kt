package com.soocedu.datacontroller.manager

import com.soocedu.datacontroller.CustomApplication
import com.soocedu.datacontroller.bean.UserInfo
import com.soocedu.datacontroller.constant.PreferenceKey

/*
 * 文件名: LoginSession
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [登录缓存，用来缓存用户数据,该类为单例]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
object LoginSession {

    private var mUserInfo = UserInfo()

    /**
     * 保存用户登录信息
     * @param userInfo 用户信息
     * @param userName 用户姓名
     * @param password 密码
     */
    fun login(userInfo: UserInfo, userName: String, password: String) {
        mUserInfo = userInfo
        CustomApplication.preference.putString(PreferenceKey.KEY_PASSWORD, password)
                .putString(PreferenceKey.KEY_USER_NAME, userName)
                .putString(PreferenceKey.KEY_OAUTH_TOKEN, userInfo.access_token)
                .apply()
    }

    fun userInfo(): UserInfo {
        return mUserInfo
    }

    fun quit() {
        CustomApplication.preference.remove(PreferenceKey.KEY_PASSWORD)
                .remove(PreferenceKey.KEY_USER_NAME)
                .remove(PreferenceKey.KEY_OAUTH_TOKEN)
                .apply()
    }


}
