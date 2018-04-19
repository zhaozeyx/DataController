package com.demon.scan.codebar;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.demon.scan.R;
import com.demon.scan.codebar.camera.CameraManager;
import com.demon.scan.codebar.decoding.CaptureActivityHandler;
import com.demon.scan.codebar.decoding.InactivityTimer;
import com.demon.scan.codebar.view.ViewfinderView;
import com.google.zxing.Result;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.io.IOException;


/**
 * 扫码基类
 */
public abstract class CodeBarCaptureBaseActivity extends BaseActivity implements Callback, ICodeBarView {
    private static final String TAG = "CodeBarCaptureActivity";

    /**
     * 扫描成功后的提示音量
     */
    private static final float BEEP_VOLUME = 0.10f;

    /**
     * 扫描成功后的提示震动持续时间
     */
    private static final long VIBRATE_DURATION = 200L;

    /**
     * 界面展示的Handler
     */
    private CaptureActivityHandler mCaptureActivityHandler;

    /**
     * 是否处于预览状态
     */
    private boolean mHasSurface;

    /**
     * 二维码图像数据读取器
     */
    ImageScanner mImageScanner;

    /**
     * 对长时间不再活动的Activity进行管理的工具类
     */
    private InactivityTimer mInactivityTimer;

    /**
     * 扫描成功后提示音的播放
     */
    private MediaPlayer mMediaPlayer;

    /**
     * 扫描成功后是否有提示音
     */
    private boolean mPlayBeep;

    /**
     * 扫描成功后是否有震动
     */
    private boolean mVibrate;

    static {
        System.loadLibrary("iconv");
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        /**
         * 提示音播放完的回调方法<BR>
         * @param mediaPlayer 播放器
         */
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager.init(getApplication());
        mImageScanner = new ImageScanner();
        mImageScanner.setConfig(0, Config.X_DENSITY, 3);
        mImageScanner.setConfig(0, Config.Y_DENSITY, 3);
        mInactivityTimer = new InactivityTimer(this);
    }

    /**
     * 初始化相机配置
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
            if (mCaptureActivityHandler == null) {
                mCaptureActivityHandler = new CaptureActivityHandler(this, null, null);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (RuntimeException e) {
            showPermissionDeniedDialog();
            finish();
        }
    }

    public abstract void showPermissionDeniedDialog();

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = getSurfaceView();
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (mHasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        mPlayBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            mPlayBeep = false;
        }
        initBeepSound();
        mVibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCaptureActivityHandler != null) {
            mCaptureActivityHandler.quitSynchronously();
            mCaptureActivityHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    /**
     * 初始化振动播放器
     */
    private void initBeepSound() {
        if (mPlayBeep && mMediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(),
                        file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }
    }

    /**
     * 扫描成功的振动提示
     */
    private void playBeepSoundAndVibrate() {
        if (mPlayBeep && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
        if (mVibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    //SurfaceHolder.Callback 方法
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mHasSurface) {
            mHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurface = false;
    }

    /**
     * 扫描成功后重启扫描
     */
    public void restartScan() {
        if (mCaptureActivityHandler != null) {
            Message message = Message.obtain(mCaptureActivityHandler, R.id.restart_preview);
            message.sendToTarget();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放MediaPlayer资源
        if (null != mMediaPlayer) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        if (null != mInactivityTimer) {
            mInactivityTimer.shutdown();
            mInactivityTimer = null;
        }
    }

    //抽象方法
    protected abstract void handleScanResult(String result);

    @Override
    public abstract ViewfinderView getViewfinderView();

    public abstract SurfaceView getSurfaceView();

    @Override
    public ImageScanner getImageScanner() {
        return mImageScanner;
    }

    @Override
    public Handler getCodeBarHandler() {
        return mCaptureActivityHandler;
    }

    /**
     * 开始预览的时候的初始化操作<BR>
     */
    @Override
    public void drawViewfinder() {
        getViewfinderView().drawViewfinder();
    }

    /**
     * 对扫描解析出来的结果进行处理
     *
     * @param resultObj 扫描出来的结果
     */
    @Override
    public void handleDecode(Object resultObj) {
        playBeepSoundAndVibrate();
        if (resultObj instanceof Result) {
            Log.e(TAG, "decode by zxing");
            Result resultZxing = (Result) resultObj;
            handleScanResult(resultZxing.getText());
        } else if (resultObj instanceof SymbolSet) {
            Log.e(TAG, "decode by zbar");
            SymbolSet resultSet = (SymbolSet) resultObj;
            for (Symbol sym : resultSet) {
                handleScanResult(sym.getData());
            }
        }

        if (null != mInactivityTimer) {
            mInactivityTimer.onActivity();
        }
    }
}
