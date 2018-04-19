package com.soocedu.datacontroller.repository

import com.soocedu.datacontroller.service.AppService

/*
 * 文件名: LoginRepository
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class LoginRepository(service: AppService) {

    var appService: AppService = service

//    fun login(account: String, password: String): LiveData<ResponseModel<UserInfo>> {
//        val flowable: Flowable<ResponseModel<UserInfo>> = appService.login().subscribeOn(AndroidSchedulers.mainThread())
//        return LiveDataReactiveStreams.fromPublisher(flowable)
//    }
}