package com.caldremch.android.dynamic.host

import android.app.Application
import com.caldremch.android.dynamic.host.manager.Shadow
import com.tencent.shadow.core.common.LoggerFactory
import com.tencent.shadow.dynamic.host.PluginManager
import java.io.File

/**
 *
 * @auther Caldremch
 *
 * @email finishmo@qq.com
 *
 * @date 2021/3/27 13:03
 *
 * @description
 *
 *
 */
class HostApp : Application() {


    companion object {
        private lateinit var sApp: HostApp
        fun getApp() = sApp
    }

    fun getPluginManager(): PluginManager =
        mPluginManager ?: throw NullPointerException("plugin manager isn't loaded")

    private var mPluginManager: PluginManager? = null

    override fun onCreate() {
        super.onCreate()
        sApp = this
        LoggerFactory.setILoggerFactory(AndroidLogLoggerFactory())
        LoadPluginHelper.downloadPluginData(this)
    }

    fun loadPluginManager(apk: File) {
        if (mPluginManager == null) {
            mPluginManager = Shadow.getPluginManager(apk)
        }
    }
}