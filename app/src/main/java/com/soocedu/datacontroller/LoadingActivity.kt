package com.soocedu.datacontroller

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.soocedu.datacontroller.bean.UserInfo
import com.soocedu.datacontroller.constant.PreferenceKey
import com.soocedu.datacontroller.presenter.LoginPresenter
import com.soocedu.datacontroller.ui.BaseActivity
import com.soocedu.datacontroller.ui.deviceList.DeviceListActivity
import com.soocedu.datacontroller.ui.login.LoginActivity
import com.soocedu.datacontroller.ui.login.view.ILoginView

/*
 * 文件名: LoadingActivity
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class LoadingActivity : BaseActivity(), ILoginView {

    private var mLoginPresenter: LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        mLoginPresenter = LoginPresenter(this)
//        initPager()
        goToNextPage()
    }

    private fun goToNextPage() {
        val userName = CustomApplication.preference.getString(PreferenceKey.KEY_USER_NAME, "")
        val password = CustomApplication.preference.getString(PreferenceKey.KEY_PASSWORD, "")
        if (userName.isNullOrEmpty() || password.isNullOrEmpty()) {
            performLoginActivity()
        } else {
            performAutoLogin(userName, password)
        }
    }

    private fun performLoginActivity() {
        startActivity(Intent(this@LoadingActivity, LoginActivity::class.java))
        finish()
    }

    private fun performAutoLogin(userName: String, password: String) {
        mLoginPresenter?.login(userName, password)
    }

    override fun onDestroy() {
        super.onDestroy()
        mLoginPresenter?.deAttachView()
    }

    override fun onLoginSuccess(userInfo: UserInfo) {
        startActivity(Intent(this@LoadingActivity, DeviceListActivity::class.java))
        finish()
    }

    override fun onRequestError(code: Int?, message: String?) {
        Toast.makeText(this@LoadingActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onHttpError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
