//package com.caldremch.android.version
//
//import com.android.build.gradle.AppExtension
//import com.android.build.gradle.internal.dsl.BuildType
//import com.android.build.gradle.internal.dsl.SigningConfig
//import com.android.builder.core.BuilderConstants
//import com.android.ide.common.signing.KeystoreHelper
//import org.gradle.api.NamedDomainObjectContainer
//import org.gradle.api.Project
//
///**
// *
// * @author Caldremch
// *
// * @date 2020-08-18
// *
// * @email caldremch@163.com
// *
// * @describe
// *
// **/
//class LaboratoryPlugin : BasePlugin<Project>() {
//
//    override fun apply(project: Project) {
//
//        var isApp = true
//        val taskName = project.displayName
//
//        if (!project.hasProperty("isApp")) {
//            println("$taskName is running as Library")
//        } else {
//            isApp = getBoolean(project.properties["isApp"] as String)
//        }
//
////        val appPlugin = AppPlugin()
////        appPlugin.apply(project)
////        val libraryPlugin = LibraryPlugin()
////        libraryPlugin.apply(project)
//
//        if (isApp) {
//            println("$taskName is running as Application")
//            configSign(project)
//        }
//    }
//
//
//    /**
//     * @param isApp 组件以App运行时, 自动添加签名, 解除繁琐操作
//     */
//    private fun configSign(project: Project) {
//        //获取 buildTypes 节点
//        val android = project.extensions.getByType(AppExtension::class.java)
//        val buildTypesClosure: NamedDomainObjectContainer<BuildType> = android.buildTypes
//
//        println("default key folder: ${KeystoreHelper.defaultDebugKeystoreLocation()}")
//        val debugConfig = buildTypesClosure.getByName(BuilderConstants.DEBUG).apply {
//        }
//        val debugSignConfig = debugConfig.signingConfig
//        val tempSign = PluginUtils.copySign(debugSignConfig)
//        val releaseSign = SigningConfig(BuilderConstants.RELEASE).initWith(tempSign)
//        //给release 添加默认的证书和混淆打开
//        val releaseConfig = buildTypesClosure.getByName(BuilderConstants.RELEASE).apply {
//            isMinifyEnabled = true
//            signingConfig = releaseSign
//        }
//
//        println("debugConfig = ${debugConfig.toString()}")
//        println("releaseConfig = ${releaseConfig.toString()}")
//
//    }
//}