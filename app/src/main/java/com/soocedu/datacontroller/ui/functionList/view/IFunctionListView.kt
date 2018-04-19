package com.soocedu.datacontroller.ui.functionList.view

import com.soocedu.datacontroller.bean.NodeModel
import com.soocedu.datacontroller.view.IBaseView

/*
 * 文件名: IFunctionListView
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/18
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
interface IFunctionListView : IBaseView {
    fun onGetFunctionList(list: List<NodeModel>)
    fun onPostCommandSuccess()
}