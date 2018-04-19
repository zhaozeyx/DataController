package com.soocedu.datacontroller.ui.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.widget.Toolbar
import android.view.SurfaceView
import com.demon.scan.codebar.CodeBarCaptureBaseActivity
import com.demon.scan.codebar.view.ViewfinderView
import com.soocedu.datacontroller.R
import com.soocedu.datacontroller.ui.deviceBind.DeviceBindActivity
import kotlinx.android.synthetic.main.codebar_capture_activity.*
import timber.log.Timber

/*
 * 文件名: ScanActivity
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/9
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class ScanActivity : CodeBarCaptureBaseActivity() {
    private var mToolBar: Toolbar? = null

    companion object {
        private const val FUTURE_INTERVAL = 60 * 1000L
        private const val REQUEST_CODE_BIND_DEVICE = 0x01
    }

    private var mTimer: ScanTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.codebar_capture_activity)
        mTimer = ScanTimer(FUTURE_INTERVAL, FUTURE_INTERVAL)
        initToolBar()
    }

    private fun initToolBar() {
        mToolBar = toolbar as Toolbar
        setSupportActionBar(mToolBar)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mToolBar?.let {
            it.title = "扫码"
            it.setNavigationIcon(R.mipmap.ic_navigate_back)
            it.setNavigationOnClickListener {
                finish()
            }
        }

    }

    override fun showPermissionDeniedDialog() {

    }

    override fun handleScanResult(result: String) {
        mTimer?.cancel()
//        restartScan()
//        mTimer?.start()
//        Toast.makeText(this@ScanActivity, result, Toast.LENGTH_SHORT).show()
        Timber.d("mac : --> $result")
        startActivityForResult(DeviceBindActivity.makeIntent(this@ScanActivity, result), REQUEST_CODE_BIND_DEVICE)
    }

    override fun getViewfinderView(): ViewfinderView? {
        return viewfinder_view
    }

    override fun getSurfaceView(): SurfaceView? {
        return preview_view
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer?.cancel()
        mTimer = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_BIND_DEVICE -> if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private inner class ScanTimer(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            restartScan()
            mTimer?.start()
        }

        override fun onTick(p0: Long) {
            // DO NOTHING
        }

    }
}
