package com.soocedu.datacontroller.presenter

import com.soocedu.datacontroller.bean.DeviceModel
import com.soocedu.datacontroller.service.RetrofitFactory
import com.soocedu.datacontroller.subscribers.NetHandleSubscriber
import com.soocedu.datacontroller.ui.deviceList.view.IDeviceListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

/*
 * 文件名: DeviceListPresenter
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/16
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class DeviceListPresenter : IPresenter<IDeviceListView> {
    private val mAppService = RetrofitFactory.createAppService()
    private val disposables = CompositeDisposable()
    private var deviceListView: IDeviceListView? = null

    override fun attachView(view: IDeviceListView) {
        deviceListView = view
    }

    fun getDeviceList() {
//        val disposable = mAppService.getDeviceList().observeOn(AndroidSchedulers.mainThread()).subscribe({
//            when (it.status) {
//                NetConstant.REQUEST_FAILED -> deviceListView?.onRequestError(it.status, it.message)
//                NetConstant.REQUEST_SUCCESS -> deviceListView?.onSuccess(it.data)
//                NetConstant.REQUEST_UN_LOGIN -> deviceListView?.onLoginError(it.status, it.message)
//            }
//        }, {
//            Timber.e(it)
//            if (it is HttpException) {
//                Timber.d("code is ${it.code()}  and message is ${it.message()}")
//                when (it.code()) {
//                    NetConstant.REQUEST_FAILED -> deviceListView?.onRequestError(it.code(), it.message)
//                    NetConstant.REQUEST_UN_LOGIN -> deviceListView?.onLoginError(it.code(), it.message)
//                    else -> deviceListView?.onHttpError()
//                }
//            } else {
//                deviceListView?.onHttpError()
//            }
//        }, {
//
//        })

        val disposable = mAppService.getDeviceList().observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : NetHandleSubscriber<List<DeviceModel>>() {
            override fun onSuccess(t: List<DeviceModel>?) {
                deviceListView?.onSuccess(t)
            }

            override fun onApiError(code: Int?, message: String?) {
                super.onApiError(code, message)
                deviceListView?.onRequestError(code, message)
            }

            override fun onLoginError(status: Int, message: String?) {
                super.onLoginError(status, message)
                deviceListView?.onLoginError(status, message)
            }

        })
        disposables.add(disposable)
    }

    override fun deAttachView() {
        disposables.dispose()
    }
}