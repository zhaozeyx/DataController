package com.soocedu.datacontroller.bean

/*
 * 文件名: UserInfo
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
class UserInfo {

    /**
     * 用户ID
     */
    var id: Int = 0
    /**
     * 用户账号
     */
    var username: String? = null
    /**
     * 过期时间
     */
    var outdated: Int = 0
    /**
     * token
     */
    var access_token: String? = null
    var expired_at: Int = 0
    var intro: String? = null
    var status: Int = 0
    var created_at: Int = 0
    var updated_at: Int = 0
}
