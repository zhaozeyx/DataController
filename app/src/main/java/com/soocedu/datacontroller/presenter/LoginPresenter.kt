package com.soocedu.datacontroller.presenter

import com.soocedu.datacontroller.bean.UserInfo
import com.soocedu.datacontroller.manager.LoginSession
import com.soocedu.datacontroller.service.AppService
import com.soocedu.datacontroller.service.RetrofitFactory
import com.soocedu.datacontroller.subscribers.NetHandleSubscriber
import com.soocedu.datacontroller.ui.login.view.ILoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

/*
 * 文件名: LoginPresenter
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [处理登录相关的Presenter]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class LoginPresenter(view: ILoginView) {
    private var loginView: ILoginView = view
    private var appService: AppService = RetrofitFactory.createAppService(false)
    private val composites: CompositeDisposable = CompositeDisposable()

    fun login(userName: String, password: String) {
        val disposable = appService.login(userName, password).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : NetHandleSubscriber<UserInfo>() {
            override fun onSuccess(t: UserInfo?) {
                t?.let {
                    LoginSession.login(it, userName, password)
                    loginView.onLoginSuccess(it)
                }
            }

            override fun onApiError(code: Int?, message: String?) {
                super.onApiError(code, message)
                loginView.onRequestError(code, message)
            }

        })
        composites.add(disposable)
    }

    fun deAttachView() {
        composites.dispose()
    }
}
