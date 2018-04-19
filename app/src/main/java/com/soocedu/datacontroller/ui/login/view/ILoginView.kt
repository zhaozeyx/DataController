package com.soocedu.datacontroller.ui.login.view

import com.soocedu.datacontroller.bean.UserInfo
import com.soocedu.datacontroller.view.IView

/*
 * 文件名: ILoginView
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
interface ILoginView : IView {
    /**
     * 登录成功
     * @param userInfo 用户信息
     */
    fun onLoginSuccess(userInfo: UserInfo)
}
