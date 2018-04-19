package com.soocedu.datacontroller.ui.deviceBind

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.soocedu.datacontroller.R
import com.soocedu.datacontroller.bean.DeviceBindModel
import com.soocedu.datacontroller.presenter.DeviceBindPresenter
import com.soocedu.datacontroller.ui.BaseToolbarActivity
import com.soocedu.datacontroller.ui.deviceBind.view.IDeviceBindView
import com.soocedu.datacontroller.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_device_bind.*

/*
 * 文件名: DeviceBindActivity
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/14
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class DeviceBindActivity : BaseToolbarActivity(), IDeviceBindView {

    private var deviceBindPresenter: DeviceBindPresenter? = null
    private var mMac: String? = null
    private var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_bind)
        deviceBindPresenter = DeviceBindPresenter(this)
        initToolBar()
        mMac = intent.getStringExtra(BUNDLE_KEY_MAC)
        initView()
    }

    private fun initView() {
        btn_submit.setOnClickListener {
            if (checkInput()) {
                mMac?.let {
                    deviceBindPresenter?.bindDevice(it, device_name.text.toString())
                }
            }
        }
    }

    private fun initToolBar() {
        setupToolbar(toolbar as Toolbar)
        setToolbarTitle("电视初次设置")
    }

    private fun checkInput(): Boolean {
        if (device_name.text.isNullOrBlank()) {
            Toast.makeText(this@DeviceBindActivity, "设备名称不能为空", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        deviceBindPresenter?.onDestroy()
    }

    companion object {
        private const val BUNDLE_KEY_MAC = "key_mac"
        fun makeIntent(context: Context, mac: String): Intent {
            val intent = Intent(context, DeviceBindActivity::class.java)
            intent.putExtra(BUNDLE_KEY_MAC, mac)
            return intent
        }
    }

    override fun onScanBindSuccess() {
        // DO NOTHING
    }

    override fun onBindSuccess(deviceBindModel: DeviceBindModel) {
        // 跳转到设备列表界面
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onBindFailed(code: Int, message: String) {
        Toast.makeText(this@DeviceBindActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestError(code: Int?, message: String?) {
        message?.let { showShortToast(message) }
    }

    override fun onHttpError() {
        showShortToast("Unknown Error")
    }

    override fun onLoginError(code: Int?, message: String?) {
        message?.let { showShortToast(it) }
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }
}