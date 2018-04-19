package com.soocedu.datacontroller.presenter

import com.soocedu.datacontroller.service.NetConstant
import com.soocedu.datacontroller.service.RetrofitFactory
import com.soocedu.datacontroller.ui.deviceBind.view.IDeviceBindView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import timber.log.Timber

/*
 * 文件名: DeviceBindPresenter
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/14
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class DeviceBindPresenter(view: IDeviceBindView) {
    private val deviceBindView = view

    private val disposables = CompositeDisposable()
    private val appService = RetrofitFactory.createAppService()

    fun bindDevice(mac: String, title: String) {
        val disposable = appService.bindDevice(mac, title).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.status) {
                        NetConstant.REQUEST_FAILED
                                or NetConstant.REQUEST_UN_LOGIN -> deviceBindView.onBindFailed(it.status, it.message!!)
                        else -> {
                            it.data?.let {
                                deviceBindView.onBindSuccess(it)
                            }
                        }
                    }
                }, {
                    Timber.e(it)
                    if (it is HttpException) {
                        Timber.d("code is ${it.code()}  and message is ${it.message()}")
                        when (it.code()) {
                            NetConstant.REQUEST_FAILED -> deviceBindView.onRequestError(it.code(), it.message)
                            NetConstant.REQUEST_UN_LOGIN -> deviceBindView.onLoginError(it.code(), it.message)
                            else -> deviceBindView.onHttpError()
                        }
                    } else {
                        deviceBindView.onHttpError()
                    }
                }, {

                })
        disposables.add(disposable)
    }

    fun onDestroy() {
        disposables.dispose()
    }
}
