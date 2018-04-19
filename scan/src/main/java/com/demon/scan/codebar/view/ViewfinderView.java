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

package com.demon.scan.codebar.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.demon.scan.R;
import com.demon.scan.codebar.camera.CameraManager;

/**
 * 绘制界面的视图
 */
public final class ViewfinderView extends View {
    /**
     * 扫描频率
     */
    private static final long ANIMATION_DELAY = 10L;

    /**
     * 每次刷新扫描线的step值
     */
    private static final int SPEEN_DISTANCE = 5;

    /**
     * 绘制圆角矩形区域时使用的渲染模式
     */
    private static final PorterDuffXfermode PORTER_DUFFXFER_MODE =
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    /**
     * 扫码窗口周边颜色
     */
    private final int mMaskColor;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 要绘制的扫描矩形区域
     */
    private Rect mBorderRect;

    /**
     * 扫描线扫描范围的top值
     */
    private int mSlideTop;

    /**
     * 扫描线扫描范围的bottom值
     */
    private int mSlideBottom;

    /**
     * 扫描射线
     */
    private Bitmap mRayLineBitmap;

    /**
     * 扫描框背景
     */
    private Bitmap mScanFrameBitmap;

    /**
     * 扫描射线所在的矩形区域
     */
    private Rect mBitmapRayRect;

    /**
     * 扫描框背景所在的矩形区域
     */
    private Rect mBitmapFrameRect;

    /**
     * 整个画布的宽度
     */
    private int mCanvasWidth;

    /**
     * 扫描线的宽度
     */
    private int mMiddleLineWidth;

    /**
     * 整个画布的宽度
     */
    private int mCanvasHeight;

    /**
     * 是否是第一次开启扫描操作
     */
    private boolean mIsFirst;

    /**
     * ViewfinderView的构造函数
     *
     * @param context 上下文
     * @param attrs   属性集
     */
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        Resources resources = getResources();
        mMaskColor = resources.getColor(android.R.color.transparent);

        mRayLineBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.uikit_scan1);

        mBitmapRayRect = new Rect(0, 0, mRayLineBitmap.getWidth(), mRayLineBitmap.getHeight());

        mMiddleLineWidth = mRayLineBitmap.getHeight();

        mScanFrameBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.uikit_scan);

        mBitmapFrameRect = new Rect(0, 0, mScanFrameBitmap.getWidth(), mScanFrameBitmap.getHeight());
    }

    public Rect getScanRect() {
        return mBorderRect;
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        //是否是第一次开启扫描
        if (!mIsFirst) {
            mBorderRect = CameraManager.get().getFramingRect();
            if (mBorderRect == null) {
                return;
            }

            mIsFirst = true;
            mSlideTop = mBorderRect.top;
            mSlideBottom = mBorderRect.bottom;

            mCanvasWidth = canvas.getWidth();
            mCanvasHeight = canvas.getHeight();
        }

        mPaint.setColor(mMaskColor);

        //绘制扫描窗口周边区域
        canvas.drawRect(0, 0, mCanvasWidth, mCanvasHeight, mPaint);

        //使用clear模式绘制扫描窗口
        mPaint.setXfermode(PORTER_DUFFXFER_MODE);
        canvas.drawRect(mBorderRect, mPaint);

        mPaint.setXfermode(null);

        //绘制扫描窗口的背景
        mPaint.setColor(Color.BLACK);
        canvas.drawBitmap(mScanFrameBitmap, mBitmapFrameRect, mBorderRect, mPaint);

        mSlideTop += SPEEN_DISTANCE;
        if (mSlideTop >= mSlideBottom) {
            mSlideTop = mBorderRect.top;
        }

        //绘制扫描线
        canvas.drawBitmap(mRayLineBitmap, mBitmapRayRect,
                new Rect(mBorderRect.left, mSlideTop - mMiddleLineWidth / 2, mBorderRect.right,
                        mSlideTop + mMiddleLineWidth / 2), mPaint);

        // 通知ondraw在指定区域刷新
        postInvalidateDelayed(ANIMATION_DELAY, mBorderRect.left, mBorderRect.top, mBorderRect.right,
                mBorderRect.bottom);
    }

    /**
     * 开始预览的时候的初始化操作<BR>
     */
    public void drawViewfinder() {
        invalidate();
    }
}
