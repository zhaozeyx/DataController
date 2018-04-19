package com.soocedu.datacontroller.ui.deviceBind.view

import com.soocedu.datacontroller.bean.DeviceBindModel
import com.soocedu.datacontroller.view.IBaseView
import com.soocedu.datacontroller.view.IView

/*
 * 文件名: IDeviceBindView
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/14
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
interface IDeviceBindView : IBaseView {
    fun onBindSuccess(deviceBindModel: DeviceBindModel)

    fun onBindFailed(code: Int, message: String)
}