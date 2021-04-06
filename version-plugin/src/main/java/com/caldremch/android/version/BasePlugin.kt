package com.caldremch.android.version

import org.gradle.api.Plugin

/**
 *
 * @author Caldremch
 *
 * @date 2020-08-18
 *
 * @email caldremch@163.com
 *
 * @describe
 *
 **/

abstract class BasePlugin<T> : Plugin<T> {


    protected fun getBoolean(value: String): Boolean {
        if (value.trim() == "true") {
            return true
        }
        return false
    }

}