package com.soocedu.datacontroller.view

/*
 * 文件名: IView
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
interface IView {
    /**
     * 请求错误:处理业务逻辑错误
     * @param code 错误码
     * @param message 消息
     */
    fun onRequestError(code: Int?, message: String?)

    /**
     * 网络请求错误:处理404 501之类的网络错误
     */
    fun onHttpError()
}