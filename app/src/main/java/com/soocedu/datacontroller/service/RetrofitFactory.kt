package com.soocedu.datacontroller.service

import android.util.Log
import com.demon.library.utils.StringUtils
import com.soocedu.datacontroller.CustomApplication
import com.soocedu.datacontroller.constant.PreferenceKey
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/*
 * 文件名: RetrofitFactory
 * 版    权：  Copyright Sooc. Co. Ltd. All Rights Reserved.
 * 描    述: [该类的简要描述]
 * 创建人: zhaozeyang
 * 创建时间:2018/4/10
 *
 * 修改人：
 * 修改时间:
 * 修改内容：[修改内容]
 */
object RetrofitFactory {
    private const val TAG = "RetrofitFactory"
    private val preferences = CustomApplication.preference
    //
    fun createAppService(isToken: Boolean = true): AppService {
        val baseUrl = StringUtils.separator(preferences.getString(PreferenceKey.KEY_SERVER_URL, ""))
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .client(createClient(isToken)).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers
                        .newThread())).build()
        return retrofit.create(AppService::class.java)
    }

    private fun createClient(isToken: Boolean = true): OkHttpClient {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.d(TAG, "\n" + message) })
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor { chain ->
                    var request = chain.request()
                    var tokenBuilder: HttpUrl.Builder = request.url().newBuilder().scheme(request.url().scheme()).host(request.url().host())
                    if (isToken) {
                        tokenBuilder = request.url().newBuilder().addQueryParameter("access-token", preferences.getString(PreferenceKey.KEY_OAUTH_TOKEN, ""))
                    }
                    // IM 交互需要该ID
                    request = request.newBuilder().addHeader("Content-Type",
                            "application/json; charset=utf-8").url(tokenBuilder.build()).build()
                    chain.proceed(request)
                }
                .addInterceptor(interceptor).build()
    }
}
