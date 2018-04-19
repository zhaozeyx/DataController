package com.soocedu.datacontroller.ui.functionList.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.soocedu.datacontroller.R

/*
 * 文件名: FunctionViewHolder
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/18
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class FunctionViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    var titleView: TextView? = itemView?.findViewById(R.id.title)
    var statusView: ImageView? = itemView?.findViewById(R.id.ic_status)
    var bgStatusView: View? = itemView?.findViewById(R.id.bg_status)
    var bgView: ImageView? = itemView?.findViewById(R.id.bg_view)
}