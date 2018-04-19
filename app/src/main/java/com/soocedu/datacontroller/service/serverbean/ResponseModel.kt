package com.soocedu.datacontroller.service.serverbean

/*
 * 文件名: ResponseModel
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class ResponseModel<T> {
    var message: String? = null
    var data: T? = null
    var status: Int = -1
}
