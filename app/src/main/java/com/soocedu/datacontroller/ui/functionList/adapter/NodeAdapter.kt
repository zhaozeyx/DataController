package com.soocedu.datacontroller.ui.functionList.adapter

import android.content.Context
import android.databinding.ObservableArrayList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.soocedu.datacontroller.R
import com.soocedu.datacontroller.bean.FunctionModel
import com.soocedu.datacontroller.bean.NodeModel
import com.soocedu.datacontroller.ui.functionList.holder.FooterHolder
import com.soocedu.datacontroller.ui.functionList.holder.FunctionViewHolder
import com.soocedu.datacontroller.ui.functionList.holder.NodeViewHolder
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter

/*
 * 文件名: NodeAdapter
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/18
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class NodeAdapter(context: Context, data: List<NodeModel> = ArrayList()) : SectionedRecyclerViewAdapter<NodeViewHolder, FunctionViewHolder, FooterHolder>() {
    private val bgIds: IntArray = intArrayOf(R.mipmap.bg_function_a, R.mipmap.bg_function_b, R.mipmap.bg_function_c, R.mipmap.bg_function_d, R.mipmap.bg_function_e,
            R.mipmap.bg_function_f, R.mipmap.bg_function_g, R.mipmap.bg_function_h, R.mipmap.bg_function_i, R.mipmap.bg_function_j)

    var mSelectFunction: FunctionModel? = null
    val mData: ObservableArrayList<NodeModel> = ObservableArrayList()
    private val mContext = context
    var mOnFunctionClickListener: OnFunctionClickListener? = null

    init {
        mData.addAll(data)
    }

    override fun getSectionCount(): Int = mData.size

    override fun onCreateItemViewHolder(parent: ViewGroup?, viewType: Int): FunctionViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.function_list_function_item, parent, false)
        return FunctionViewHolder(view)
    }

    override fun hasFooterInSection(section: Int): Boolean = false

    override fun onCreateSectionFooterViewHolder(parent: ViewGroup?, viewType: Int): FooterHolder {
        return FooterHolder(null)
    }

    override fun getItemCountForSection(section: Int): Int = mData[section].items?.size ?: 0

    override fun onCreateSectionHeaderViewHolder(parent: ViewGroup?, viewType: Int): NodeViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.function_list_node_item, parent, false)
        return NodeViewHolder(view)
    }

    override fun onBindSectionFooterViewHolder(holder: FooterHolder?, section: Int) {
    }

    override fun onBindItemViewHolder(holder: FunctionViewHolder?, section: Int, position: Int) {
        val function = mData[section].items?.get(position)
        holder?.bgView?.setBackgroundResource(bgIds[position % bgIds.size])
        holder?.titleView?.text = function?.title
        holder?.statusView?.visibility = if (mSelectFunction == function) View.VISIBLE else View.GONE
        holder?.bgStatusView?.visibility = if (mSelectFunction == function) View.VISIBLE else View.GONE
        holder?.itemView?.setOnClickListener {
            mSelectFunction = function
            notifyDataSetChanged()
            function?.let {
                mOnFunctionClickListener?.onFunctionClicked(mData[section], it)
            }
        }
    }

    override fun onBindSectionHeaderViewHolder(holder: NodeViewHolder?, section: Int) {
        holder?.titleView?.text = mData[section].title
    }

    interface OnFunctionClickListener {
        fun onFunctionClicked(nodeModel: NodeModel, functionModel: FunctionModel)
    }
}