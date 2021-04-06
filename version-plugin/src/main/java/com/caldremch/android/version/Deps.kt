package com.caldremch.android.version

import org.gradle.api.Project

/**
 *
 * @author Caldremch
 *
 * @date 2020-05-29 14:28
 *
 * @email caldremch@163.com
 *
 * @describe 依赖库管理
 *
 **/

object Plugin {
    const val application = "com.android.application"
    const val library = "com.android.library"
    const val kotlin_android = "kotlin-android"
    const val kotlin_kapt = "kotlin-kapt"
    const val kotlin = "kotlin"
    const val java = "java"
}

object BintrayConst {
    const val myBintrayName = "myBintrayName"
    const val myArtifactId = "myArtifactId"
    const val myLibraryName = "myLibraryName"
    const val myLibraryDescription = "myLibraryDescription"
    const val myLibraryVersion = "myLibraryVersion"
}

object Deps {

    //maven publication name
    const val a_name_whatever_you_want = "a_name_whatever_you_want"

    //version
    const val minSdkVersion = 21
    const val compileSdkVersion = 30
    const val targetSdkVersion = 30
    const val versionCode = 1
    const val versionName = "1.0"
    const val applicationId = "com.caldremch.android.laboratory"
    const val buildToolsVersion = "30.0.2"
    const val kotlin_version = "1.4.31"
    const val bintray_plugin_version = "1.8.5"

    //support
    const val appcompat = "androidx.appcompat:appcompat:1.1.0"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:1.1.3"
    const val cardview = "androidx.cardview:cardview:1.0.0"
    const val ktx = "androidx.core:core-ktx:1.3.0"
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    const val lifecycle = "androidx.lifecycle:lifecycle-common-java8:2.2.0"
    const val design = "com.google.android.material:material:1.1.0"
    const val gson = "com.google.code.gson:gson:2.6.2"

    //android dep
    const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0"

    //third part
    const val glide = "com.github.bumptech.glide:glide:4.10.0"
    const val recyclerview_animators = "jp.wasabeef:recyclerview-animators:3.0.0"
    const val immersionbar = "com.gyf.immersionbar:immersionbar:3.0.0"
    const val BaseRecyclerViewAdapterHelper =
        "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4"
    const val lottie = "com.airbnb.android:lottie:3.4.1"
    const val eventbus = "org.greenrobot:eventbus:3.2.0"
    const val refresh = "com.scwang.smart:refresh-layout-kernel:2.0.1"
    const val refresh_header_falsify = "com.scwang.smart:refresh-header-falsify:2.0.1"
    const val refresh_footer_classics = "com.scwang.smart:refresh-footer-classics:2.0.1"
    const val refresh_header_classics = "com.scwang.smart:refresh-header-classics:2.0.1"
    const val banner = "com.youth.banner:banner:2.0.12"
    const val MagicIndicator = "com.github.hackware1993:MagicIndicator:1.6.0"
    const val leakcanary = "com.squareup.leakcanary:leakcanary-android:2.4"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9"
    const val viewbinding = "com.kirich1409.viewbindingpropertydelegate:vbpd-noreflection:1.4.1"


    //maven url
    const val maven_aliyun_public = "http://maven.aliyun.com/nexus/content/groups/public/"
    const val maven_aliyun_jcenter = "https://maven.aliyun.com/nexus/content/repositories/jcenter"
    const val maven_aliyun_google = "https://maven.aliyun.com/nexus/content/repositories/google"
    const val maven_jitpack = "https://jitpack.io"

    //plugin
    const val auto_service = "com.google.auto.service:auto-service:1.0-rc6"
    const val javapoet = "com.squareup:javapoet:1.10.0"
    const val kotlin_stdlib_jdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:jdk8-$kotlin_version"

    const val entry = "com.caldremch.android:entry:1.5.0"
    const val entry_compiler = "com.caldremch.android:entry-compiler:1.5.1"

    //支持result register
    const val activity = "androidx.activity:activity-ktx:1.2.0-rc01"
    const val fragment = "androidx.fragment:fragment-ktx:1.3.0-rc01"

    /**
     * 添加默认的maven url

    fun addDefaultRepo(repositoryHandler: RepositoryHandler) {
    //本地仓库
    repositoryHandler.mavenLocal()
    //优先查找镜像地址
    repositoryHandler.maven(url = maven_aliyun_public)
    repositoryHandler.maven(url = maven_aliyun_jcenter)
    repositoryHandler.maven(url = maven_aliyun_google)
    //官方地址
    repositoryHandler.mavenCentral()
    repositoryHandler.google()
    repositoryHandler.jcenter()
    repositoryHandler.maven { setUrl(Deps.maven_jitpack) }

    }*/

    /**
     * find properties in local.properties
     */
    fun findInLocalProperties(project: Project, key: String): String? {
        val properties = java.util.Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        return properties.getProperty(key)
    }

    fun addUsualDep() {

    }
}

