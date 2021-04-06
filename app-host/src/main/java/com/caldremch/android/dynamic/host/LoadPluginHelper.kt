package com.caldremch.android.dynamic.host

import android.content.Context
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 *
 * @auther Caldremch
 *
 * @email finishmo@qq.com
 *
 * @date 2021/3/27 13:07
 *
 * @description
 *
 *
 */
object LoadPluginHelper {

    /**
     * 动态加载的插件管理apk
     */
    const val sPluginManagerName = "app-plugin-manager.apk"

    /**
     * 动态加载的插件包，里面包含以下几个部分，插件apk，插件框架apk（loader apk和runtime apk）, apk信息配置关系json文件
     */
//    val sPluginZip = if (BuildConfig.DEBUG) "plugin-debug.zip" else "plugin-release.zip"
    const val sPluginZip = "plugins.zip"


    private var singlePool = Executors.newSingleThreadExecutor()

    lateinit var pluginManagerFile: File

    lateinit var pluginZipFile: File


    fun getSinglePool(): ExecutorService {
        return singlePool
    }

    /**
     * 1.下载最新的插件(各个模块)
     * 2.存放到对应目录
     * 等待需要时加载对应的文件
     */
    fun downloadPluginData(context: Context) {
        pluginManagerFile =
            File(context.filesDir, sPluginManagerName)
        pluginZipFile =
            File(context.filesDir, sPluginZip)

        singlePool.execute { preparePlugin(context) }
    }

    private fun preparePlugin(context: Context) {
        try {
            val `is`: InputStream = context.getAssets()
                .open(sPluginManagerName)
            org.apache.commons.io.FileUtils.copyInputStreamToFile(`is`, pluginManagerFile)
            val zip: InputStream =
                context.assets.open(sPluginZip)
            org.apache.commons.io.FileUtils.copyInputStreamToFile(zip, pluginZipFile)
        } catch (e: IOException) {
            throw RuntimeException("从assets中复制apk出错", e)
        }
    }
}