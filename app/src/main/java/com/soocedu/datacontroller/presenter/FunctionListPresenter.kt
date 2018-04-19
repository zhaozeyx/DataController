package com.soocedu.datacontroller.presenter

import com.soocedu.datacontroller.service.NetConstant
import com.soocedu.datacontroller.service.RetrofitFactory
import com.soocedu.datacontroller.ui.functionList.view.IFunctionListView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import timber.log.Timber

/*
 * 文件名: FunctionListPresenter
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/18
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class FunctionListPresenter : IPresenter<IFunctionListView> {
    private val mAppService = RetrofitFactory.createAppService()
    private val mDisposables = CompositeDisposable()
    private var mView: IFunctionListView? = null

    fun getFunctionList() {
        val disposable = mAppService.getFunctionList().observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.status) {
                        NetConstant.REQUEST_FAILED -> mView?.onRequestError(it.status, it.message)
                        NetConstant.REQUEST_SUCCESS -> mView?.onGetFunctionList(it.data!!)
                        NetConstant.REQUEST_UN_LOGIN -> mView?.onLoginError(it.status, it.message)
                    }
                }, {
                    Timber.e(it)
                    if (it is HttpException) {
                        Timber.d("code is ${it.code()}  and message is ${it.message()}")
                        when (it.code()) {
                            NetConstant.REQUEST_FAILED -> mView?.onRequestError(it.code(), it.message)
                            NetConstant.REQUEST_UN_LOGIN -> mView?.onLoginError(it.code(), it.message)
                            else -> mView?.onHttpError()
                        }
                    } else {
                        mView?.onHttpError()
                    }
                }, {

                })
        mDisposables.add(disposable)
    }

    fun postCommand(mac: String, nodeId: String, itemId: String) {
        val disposable = mAppService.postCommand(mac, nodeId, itemId).observeOn(AndroidSchedulers.mainThread()).subscribe({
            when (it.status) {
                NetConstant.REQUEST_FAILED -> mView?.onRequestError(it.status, it.message)
                NetConstant.REQUEST_SUCCESS -> mView?.onPostCommandSuccess()
                NetConstant.REQUEST_UN_LOGIN -> mView?.onLoginError(it.status, it.message)
            }
        }, {
            Timber.e(it)
            if (it is HttpException) {
                Timber.d("code is ${it.code()}  and message is ${it.message()}")
                when (it.code()) {
                    NetConstant.REQUEST_FAILED -> mView?.onRequestError(it.code(), it.message)
                    NetConstant.REQUEST_UN_LOGIN -> mView?.onLoginError(it.code(), it.message)
                    else -> mView?.onHttpError()
                }
            } else {
                mView?.onHttpError()
            }
        }, {})
    }

    override fun attachView(view: IFunctionListView) {
        mView = view
    }

    override fun deAttachView() {
        mView = null
        mDisposables.dispose()
    }

}