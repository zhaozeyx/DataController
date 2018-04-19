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

package com.demon.scan.codebar.decoding;

import android.app.Activity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Finishes an activity after a period of inactivity.
 */
public final class InactivityTimer {

  private static final int INACTIVITY_DELAY_SECONDS = 5 * 60;

  private final ScheduledExecutorService inactivityTimer =
      Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());

  private final Activity activity;

  private ScheduledFuture<?> inactivityFuture = null;

  /**
   * [构造简要说明]
   *
   * @param activity 要终止的Activity
   */
  public InactivityTimer(Activity activity) {
    this.activity = activity;
    onActivity();
  }

  /**
   * 终止activity的回调方法
   */
  public void onActivity() {
    cancel();
    inactivityFuture =
        inactivityTimer.schedule(new FinishListener(activity), INACTIVITY_DELAY_SECONDS,
            TimeUnit.SECONDS);
  }

  private void cancel() {
    if (inactivityFuture != null) {
      inactivityFuture.cancel(true);
      inactivityFuture = null;
    }
  }

  /**
   * 关闭线程池
   */
  public void shutdown() {
    cancel();
    inactivityTimer.shutdown();
  }

  /**
   * 创建后台线程的工厂类
   *
   * @author jiazhenkai
   * @version [Paitao Client V20130911, 2014-3-31]
   */
  private static final class DaemonThreadFactory implements ThreadFactory {
    public Thread newThread(Runnable runnable) {
      Thread thread = new Thread(runnable);
      thread.setDaemon(true);
      return thread;
    }
  }
}
