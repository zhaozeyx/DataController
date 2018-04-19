package com.soocedu.datacontroller.subscribers

import com.soocedu.datacontroller.service.NetConstant
import com.soocedu.datacontroller.service.serverbean.ResponseModel
import io.reactivex.subscribers.ResourceSubscriber
import retrofit2.HttpException

/*
 * 文件名: NetHandleSubscriber
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
abstract class NetHandleSubscriber<S> : ResourceSubscriber<ResponseModel<S>>() {


    override fun onComplete() {
    }

    override fun onNext(t: ResponseModel<S>?) {
        when (t?.status) {
            NetConstant.REQUEST_FAILED -> onApiError(t.status, t.message)
            NetConstant.REQUEST_UN_LOGIN -> onLoginError(t.status, t.message)
            else -> onSuccess(t?.data)
        }
    }

    override fun onError(t: Throwable?) {
        if (t is HttpException) {
            when (t.response().code()) {
                NetConstant.REQUEST_FAILED -> onApiError(t.response().code(), t.response().message())
                NetConstant.REQUEST_UN_LOGIN -> onLoginError(t.response().code(), t.response().message())
                else -> onHttpError()
            }
        }
    }

    abstract fun onSuccess(t: S?)

    open fun onApiError(code: Int?, message: String?) {
    }

    open fun onHttpError() {

    }

    open fun onLoginError(status: Int, message: String?) {

    }
}