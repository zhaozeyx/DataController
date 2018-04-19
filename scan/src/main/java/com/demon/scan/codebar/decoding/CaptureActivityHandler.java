/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.demon.scan.codebar.decoding;

import android.os.Handler;
import android.os.Message;

import com.demon.scan.R;
import com.demon.scan.codebar.ICodeBarView;
import com.demon.scan.codebar.camera.CameraManager;
import com.google.zxing.BarcodeFormat;

import java.lang.ref.WeakReference;
import java.util.Vector;

/**
 * This class handles all the messaging which comprises the state machine for capture.
 */
public final class CaptureActivityHandler extends Handler {
    private final DecodeThread decodeThread;

    private final WeakReference<ICodeBarView> mWeakCodeBarView;

    private State state;

    /**
     * 扫描状态<BR>
     *
     * @author jiazhenkai
     * @version [Paitao Client V20130911, 2014-1-4]
     */
    private enum State {
        PREVIEW, SUCCESS, DONE
    }

    /**
     * 对扫描后解析出的字符串进行处理<BR>
     *
     * @param codeBarView      扫描界面对应的Activity
     * @param decodeFormats 解码格式
     * @param characterSet  字符集
     */
    public CaptureActivityHandler(ICodeBarView codeBarView,
                                  Vector<BarcodeFormat> decodeFormats, String characterSet) {
        mWeakCodeBarView = new WeakReference<ICodeBarView>(codeBarView);
        decodeThread = new DecodeThread(codeBarView, decodeFormats, characterSet);
        decodeThread.start();
        state = State.SUCCESS;
        // Start ourselves capturing previews and decoding.
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override
    public void handleMessage(Message message) {
        if (message.what == R.id.auto_focus) {
            // When one auto focus pass finishes, start another. This is the closest thing to
            // continuous AF. It does seem to hunt a bit, but I'm not sure what else to do.
            if (state == State.PREVIEW) {
                CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            }
        } else if (message.what == R.id.restart_preview) {
            restartPreviewAndDecode();
        } else if (message.what == R.id.decode_succeeded) {
            state = State.SUCCESS;
            ICodeBarView codeBarView = mWeakCodeBarView.get();
            if (null != codeBarView) {
                codeBarView.handleDecode(message.obj);
            }
        } else if (message.what == R.id.decode_failed) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        }
    }

    /**
     * 扫描完成，终止预览，回收资源<BR>
     * [功能详细描述]
     */
    public void quitSynchronously() {
        state = State.DONE;
        CameraManager.get().stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            decodeThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 清除消息
        removeCallbacksAndMessages(null);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            CameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            ICodeBarView codeBarView = mWeakCodeBarView.get();
            if (null != codeBarView) {
                codeBarView.drawViewfinder();
            }
        }
    }
}
