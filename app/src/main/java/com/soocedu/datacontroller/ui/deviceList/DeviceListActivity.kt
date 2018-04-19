package com.soocedu.datacontroller.ui.deviceList

import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.soocedu.datacontroller.R
import com.soocedu.datacontroller.bean.DeviceModel
import com.soocedu.datacontroller.databinding.ActivityDeviceListBinding
import com.soocedu.datacontroller.databinding.DeviceListItemBinding
import com.soocedu.datacontroller.presenter.DeviceListPresenter
import com.soocedu.datacontroller.ui.BaseToolbarActivity
import com.soocedu.datacontroller.ui.deviceList.view.IDeviceListView
import com.soocedu.datacontroller.ui.functionList.FunctionListActivity
import com.soocedu.datacontroller.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_device_list.*

/*
 * 文件名: DeviceListActivity
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/13
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class DeviceListActivity : BaseToolbarActivity(), IDeviceListView {

    private var mAdapter: DeviceListAdapter? = null
    private val mPresenter = DeviceListPresenter()
    private var mBinding: ActivityDeviceListBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_device_list)
        mPresenter.attachView(this)
        setupToolbar(toolbar as Toolbar)
        setToolbarTitle("设备列表")
        initList()
        initBtn()
        loadData()
    }

    private fun loadData() {
        mPresenter.getDeviceList()
    }

    private fun initList() {
        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.isAutoMeasureEnabled = true

        mBinding?.deviceList?.setHasFixedSize(true)
        mBinding?.deviceList?.isNestedScrollingEnabled = false
        mAdapter = DeviceListAdapter()
        mBinding?.deviceList?.layoutManager = layoutManager
        mBinding?.deviceList?.adapter = mAdapter
        mBinding?.setDeviceList(mAdapter?.mData)
        mBinding?.selectedList = mAdapter?.mSelectedModels
    }

    private fun initBtn() {
        mBinding?.btnChooseFunction?.setOnClickListener({
            if (!TextUtils.isEmpty(mAdapter?.getSelectedMac())) {
                mAdapter?.getSelectedMac()?.let { startActivity(FunctionListActivity.makeIntent(this, it)) }
            }
        })
    }

    override fun onScanBindSuccess() {
        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.deAttachView()
    }

    override fun onSuccess(deviceList: List<DeviceModel>?) {
        mAdapter?.clearAll()
        mAdapter?.addAll(deviceList)
        mAdapter?.notifyDataSetChanged()
    }

    override fun onRequestError(code: Int?, message: String?) {
        message?.let {
            showShortToast(it)
        }
    }

    override fun onHttpError() {
    }

    override fun onLoginError(code: Int?, message: String?) {
        message?.let {
            showShortToast(it)
        }
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private class DeviceListAdapter : RecyclerView.Adapter<DeviceItemHolder>() {

        val mSelectedModels: ObservableArrayList<DeviceModel> = ObservableArrayList()
        val mData: ObservableArrayList<DeviceModel> = ObservableArrayList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceItemHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DeviceListItemBinding.inflate(inflater, parent, false)
            return DeviceItemHolder(binding)
        }

        override fun getItemCount(): Int = mData.size

        override fun onBindViewHolder(holder: DeviceItemHolder, position: Int) {
            val model = mData[position]
            holder.bindTo(model, mSelectedModels)
            holder.itemView.setOnClickListener({
                val selected = mSelectedModels.contains(model)
                if (!selected) {
                    mSelectedModels.add(model)
                } else {
                    mSelectedModels.remove(model)
                }
            })
        }

        fun addAll(list: List<DeviceModel>?) {
            list?.let {
                mData.addAll(list)
            }
        }

        fun clearAll() {
            mData.clear()
        }

        fun getData(): ObservableArrayList<DeviceModel> {
            return mData
        }

        fun getSelectedMac(): String? {
            val sb = StringBuilder()
            for (item in mSelectedModels) {
                sb.append(item.mac).append(",")
            }
            return sb.substring(0, sb.length)
        }
    }

    private class DeviceItemHolder(binding: DeviceListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val mBinding = binding

        fun bindTo(deviceModel: DeviceModel, selectedDevices: ObservableArrayList<DeviceModel>) {
            mBinding.deviceModel = deviceModel
            mBinding.deviceList = selectedDevices
            mBinding.executePendingBindings()
        }
    }

}