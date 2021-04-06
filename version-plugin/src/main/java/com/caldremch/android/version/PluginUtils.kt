//package com.caldremch.android.version
//
//import com.android.build.gradle.internal.dsl.SigningConfig
//import java.io.File
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
//object PluginUtils {
//
//    fun copySign(debugSignConfig:SigningConfig):com.android.builder.model.SigningConfig{
//        return object : com.android.builder.model.SigningConfig{
//            override fun getStoreType(): String {
//                return debugSignConfig.storeType
//            }
//
//            override fun isV1SigningEnabled(): Boolean {
//                return debugSignConfig.isV1SigningEnabled
//
//            }
//
//            override fun isSigningReady(): Boolean {
//                return debugSignConfig.isSigningReady
//
//            }
//
//            override fun getStorePassword(): String {
//                return debugSignConfig.storePassword
//
//            }
//
//            override fun getName(): String {
//                return debugSignConfig.name
//
//            }
//
//            override fun getStoreFile(): File {
//                return debugSignConfig.storeFile
//
//            }
//
//            override fun getKeyAlias(): String {
//                return debugSignConfig.keyAlias
//
//            }
//
//            override fun getKeyPassword(): String {
//                return debugSignConfig.keyPassword
//
//            }
//
//            override fun isV2SigningEnabled(): Boolean {
//                return debugSignConfig.isV2SigningEnabled
//            }
//
//        }
//    }
//
//}