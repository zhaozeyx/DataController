package com.soocedu.datacontroller.ui.deviceList.view

import com.soocedu.datacontroller.bean.DeviceModel
import com.soocedu.datacontroller.view.IBaseView

/*
 * 文件名: IDeviceListView
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/16
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
interface IDeviceListView : IBaseView {
    fun onSuccess(deviceList: List<DeviceModel>?)
}