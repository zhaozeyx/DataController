package com.soocedu.datacontroller.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.soocedu.datacontroller.R
import com.soocedu.datacontroller.bean.UserInfo
import com.soocedu.datacontroller.presenter.LoginPresenter
import com.soocedu.datacontroller.ui.BaseActivity
import com.soocedu.datacontroller.ui.deviceList.DeviceListActivity
import com.soocedu.datacontroller.ui.login.view.ILoginView
import kotlinx.android.synthetic.main.activity_login.*

/*
 * 文件名: LoginActivity
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class LoginActivity : BaseActivity(), ILoginView {

    private var loginPresenter: LoginPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginPresenter = LoginPresenter(this@LoginActivity)
        initView()
    }

    private fun initView() {
        submit.setOnClickListener {
            if (!checkInput()) {
                return@setOnClickListener
            }
            loginPresenter?.login(account_edit.text.toString(), password_edit.text.toString())

        }
    }

    private fun checkInput(): Boolean {
        if (account_edit.text.isNullOrBlank()) {
            Toast.makeText(this@LoginActivity, "用户名不能为空", Toast.LENGTH_LONG).show()
            return false
        }
        if (password_edit.text.isNullOrBlank()) {
            Toast.makeText(this@LoginActivity, "密码不能为空", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    override fun onLoginSuccess(userInfo: UserInfo) {
        startActivity(Intent(this@LoginActivity, DeviceListActivity::class.java))
        finish()
    }

    override fun onRequestError(code: Int?, message: String?) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onHttpError() {
        TODO("提示网络错误？")
    }
}