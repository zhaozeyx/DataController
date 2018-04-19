package com.soocedu.datacontroller.service

import com.soocedu.datacontroller.bean.*
import com.soocedu.datacontroller.service.serverbean.ResponseModel

import io.reactivex.Flowable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/*
 * 文件名: AppService
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
interface AppService {

    /**
     * 登录接口
     * @param userName 账号
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("index.php?r=v1/default/login")
    fun login(@Field("username") userName: String, @Field("password") password: String): Flowable<ResponseModel<UserInfo>>

    /**
     * 绑定设备
     * @param mac mac地址
     * @param title 设备名称
     */
    @FormUrlEncoded
    @POST("index.php?r=v1/device/update")
    fun bindDevice(@Field("mac") mac: String, @Field("title") title: String): Flowable<ResponseModel<DeviceBindModel>>

    /**
     * 获取绑定设备列表
     */
    @GET("index.php?r=v1/device/index")
    fun getDeviceList(): Flowable<ResponseModel<List<DeviceModel>>>

    /**
     * 获取功能列表
     */
    @GET("index.php?r=v1/node/index")
    fun getFunctionList(): Flowable<ResponseModel<List<NodeModel>>>


    /**
     * 发送指令
     * @param mac mac地址
     * @param nodeId 节点ID
     * @param itemId 功能ID
     */
    @FormUrlEncoded
    @POST("index.php?r=v1/device/send")
    fun postCommand(@Field("mac") mac: String, @Field("node_id") nodeId: String, @Field("item_id") itemId: String): Flowable<ResponseModel<PostCommandModel>>

}
