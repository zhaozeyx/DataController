package com.demon.scan.codebar;
/*
 * 文件名: ICodeBarView
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2017/9/7
 * 
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */

import android.os.Handler;

import com.demon.scan.codebar.view.ViewfinderView;

import net.sourceforge.zbar.ImageScanner;

public interface ICodeBarView {
    Handler getCodeBarHandler();

    ImageScanner getImageScanner();

    ViewfinderView getViewfinderView();

    void handleDecode(Object resultObj);

    void drawViewfinder();
}
