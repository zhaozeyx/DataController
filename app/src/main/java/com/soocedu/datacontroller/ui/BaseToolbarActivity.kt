package com.soocedu.datacontroller.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.afollestad.materialdialogs.MaterialDialog
import com.soocedu.datacontroller.R
import com.soocedu.datacontroller.manager.LoginSession
import com.soocedu.datacontroller.ui.login.LoginActivity
import com.soocedu.datacontroller.ui.scan.ScanActivity

/*
 * 文件名: BaseToolbarActivity
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/19
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
abstract class BaseToolbarActivity : BaseActivity() {
    private var mToolbar: Toolbar? = null
    private var mTitle: CharSequence? = null
    private var mCanBack: Boolean = true
    private var mQuitDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mToolbar?.let {
            it.title = mTitle
            if (mCanBack) {
                it.setNavigationIcon(R.mipmap.ic_navigate_back)
                it.setNavigationOnClickListener {
                    onBackPressed()
                }
            }
            it.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_scan -> {
                        startActivityForResult(Intent(this, ScanActivity::class.java), REQUEST_CODE_SCAN)
                        false
                    }
                    R.id.action_quit -> {
                        showQuitConfirmDialog()
                        false
                    }

                    else -> false
                }
            }
        }
    }

    private fun showQuitConfirmDialog() {
        if (null == mQuitDialog) {
            mQuitDialog = MaterialDialog.Builder(this).content("确认退出应用吗?")
                    .positiveText("确认")
                    .positiveColor(resources.getColor(R.color.textRed))
                    .onPositive({ dialog, _ ->
                        dialog.dismiss()
                        performQuit()
                    }).negativeText("取消")
                    .negativeColor(resources.getColor(R.color.gray))
                    .onNegative({ dialog, _ ->
                        dialog.dismiss()
                    }).build()
        }
        mQuitDialog?.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mQuitDialog?.dismiss()
    }

    private fun performQuit() {
        LoginSession.quit()
        finish()
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    protected fun setupToolbar(toolbar: Toolbar) {
        mToolbar = toolbar
        setSupportActionBar(mToolbar)
    }

    protected fun setToolbarTitle(title: CharSequence?) {
        mTitle = title
        mToolbar?.let { it.title = mTitle }
    }

    protected fun setCanback(canBack: Boolean) {
        mCanBack = canBack
    }

    protected abstract fun onScanBindSuccess()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_device_list_navigate, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SCAN -> if (resultCode == Activity.RESULT_OK) {
                onScanBindSuccess()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_SCAN = 0x01
    }
}