/*
 * Copyright (C) 2010 ZXing authors
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

package com.demon.scan.codebar.camera;

import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

/**
 * 预览回调
 *
 * @author jiazhenkai
 * @version [Paitao Client V20130911, 2014-3-31]
 */
final class PreviewCallback implements Camera.PreviewCallback {
  private final CameraConfigurationManager configManager;

  private final boolean useOneShotPreviewCallback;

  private Handler mPreviewHandler;

  private int mPreviewMessage;

  PreviewCallback(CameraConfigurationManager configManager, boolean useOneShotPreviewCallback) {
    this.configManager = configManager;
    this.useOneShotPreviewCallback = useOneShotPreviewCallback;
  }

  /**
   * 设置预览时与扫描页面通信的handler
   *
   * @param previewHandler 通信的handler
   * @param previewMessage 开启预览时发送的消息
   */
  void setHandler(Handler previewHandler, int previewMessage) {
    this.mPreviewHandler = previewHandler;
    this.mPreviewMessage = previewMessage;
  }

  public void onPreviewFrame(byte[] data, Camera camera) {
    Point cameraResolution = configManager.getCameraResolution();
    if (!useOneShotPreviewCallback) {
      camera.setPreviewCallback(null);
    }
    if (mPreviewHandler != null) {
      Message message =
          mPreviewHandler.obtainMessage(mPreviewMessage, cameraResolution.x, cameraResolution.y,
              data);
      message.sendToTarget();
      mPreviewHandler = null;
    }
  }
}
