package com.soocedu.datacontroller.viewmodel

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.databinding.ObservableArrayList

/*
 * 文件名: DevicesModel
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/16
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class DevicesModel: BaseObservable() {
    @Bindable
    val deviceList : ObservableArrayList<DevicesModel> = ObservableArrayList()
}