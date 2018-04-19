package com.demon.library.utils

/*
 * 文件名: StringUtils
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/3/25
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */

import android.text.TextUtils

object StringUtils {
    /**
     * 处理 http 地址最后"/"符号
     *
     * @param url
     * @return
     */
    fun separator(url: String): String? {
        var returnValue = url
        if (!TextUtils.isEmpty(url)) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                returnValue = "http://" + url
            }
            if (url.lastIndexOf("/") < url.length - 1) {
                returnValue += "/"
            }
        }
        return returnValue
    }

    /**
     * 特殊转移字符处理
     */
    fun changeSpecialChar(str: String): String? {
        var strChar: String? = str
        if (null != strChar && "" != strChar) {
            if (strChar.contains("&")) {
                strChar = strChar.replace("&", "&amp;")
            }
            if (strChar.contains("\"")) {
                strChar = strChar.replace("\"", "&quot;")
            }
            if (strChar.contains("<")) {
                strChar = strChar.replace("<", "&lt;")
            }
            if (strChar.contains(">")) {
                strChar = strChar.replace(">", "&gt;")
            }
        }
        return strChar
    }

}
