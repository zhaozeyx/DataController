package com.soocedu.datacontroller.ui.functionList

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.Toolbar
import com.soocedu.datacontroller.R
import com.soocedu.datacontroller.bean.FunctionModel
import com.soocedu.datacontroller.bean.NodeModel
import com.soocedu.datacontroller.databinding.ActivityFunctionListBinding
import com.soocedu.datacontroller.presenter.FunctionListPresenter
import com.soocedu.datacontroller.ui.BaseToolbarActivity
import com.soocedu.datacontroller.ui.functionList.adapter.NodeAdapter
import com.soocedu.datacontroller.ui.functionList.adapter.NodeAdapter.OnFunctionClickListener
import com.soocedu.datacontroller.ui.functionList.view.IFunctionListView
import com.soocedu.datacontroller.ui.login.LoginActivity
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup
import kotlinx.android.synthetic.main.activity_function_list.*

/*
 * 文件名: FunctionListActivity
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/17
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class FunctionListActivity : BaseToolbarActivity(), IFunctionListView {


    private var mDevices: String? = null
    private var mAdapter: NodeAdapter? = null
    private var mBinding: ActivityFunctionListBinding? = null
    private var mPresenter: FunctionListPresenter = FunctionListPresenter()

    companion object {
        private const val BUNDLE_KEY_DEVICES = "bundle_key_devices"
        fun makeIntent(context: Context, devices: String): Intent {
            val intent = Intent(context, FunctionListActivity::class.java)
            intent.putExtra(BUNDLE_KEY_DEVICES, devices)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDevices = intent.getStringExtra(BUNDLE_KEY_DEVICES)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_function_list)
        mPresenter.attachView(this)
        initToolBar()
        initList()
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.deAttachView()
    }

    private fun loadData() {
        mPresenter.getFunctionList()
    }

    private fun initList() {
        mAdapter = NodeAdapter(this)
        mBinding?.functionList?.adapter = mAdapter
        mAdapter?.mOnFunctionClickListener = object : OnFunctionClickListener {
            override fun onFunctionClicked(nodeModel: NodeModel, functionModel: FunctionModel) {
//                showShortToast("mac [$mDevices] node [${nodeModel.node_id}]   function [${functionModel.item_id}]")
                mDevices?.let { nodeModel.node_id?.let { it1 -> functionModel.item_id?.let { it2 -> mPresenter.postCommand(it, it1, it2) } } }
            }
        }

        val layoutManager = GridLayoutManager(this, 2)
        val lookup = SectionedSpanSizeLookup(mAdapter, layoutManager)
        layoutManager.spanSizeLookup = lookup
        mBinding?.functionList?.layoutManager = layoutManager
        mBinding?.data = mAdapter?.mData
    }

    private fun initToolBar() {
        setupToolbar(toolbar as Toolbar)
        setToolbarTitle("选择功能")
    }

    override fun onScanBindSuccess() {
        finish()
    }

    override fun onGetFunctionList(list: List<NodeModel>) {
        mAdapter?.mData?.clear()
        mAdapter?.mData?.addAll(list)
        mAdapter?.notifyDataSetChanged()
    }

    override fun onPostCommandSuccess() {
        showShortToast("消息推送成功")
    }

    override fun onRequestError(code: Int?, message: String?) {
        message?.let { showShortToast(message) }
    }

    override fun onHttpError() {
    }

    override fun onLoginError(code: Int?, message: String?) {
        message?.let { showShortToast(message) }
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

}