package com.morcinek.xpense.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import org.jetbrains.anko.internals.AnkoInternals
import java.io.Serializable

/**
 * Copyright 2016 Tomasz Morcinek. All rights reserved.
 */
inline fun <reified T : Parcelable> Bundle.getParcelable(): T? = getParcelable(T::class.java.name)
inline fun <reified T : Serializable> Bundle.getSerializable(): T? = getSerializable(T::class.java.name) as T?

fun <T : Parcelable> Bundle.putParcelable(value: T) = putParcelable(value.javaClass.getName(), value)
fun <T : Serializable> Bundle.putSerializable(value: T) = putSerializable(value.javaClass.getName(), value)

fun <T : Parcelable> Intent.putParcelableExtra(value: T) = putExtra(value.javaClass.getName(), value)
fun <T : Serializable> Intent.putSerializableExtra(value: T) = putExtra(value.javaClass.getName(), value)

inline fun <reified T : Parcelable> Intent.getParcelableExtra() = getParcelableExtra<T>(T::class.java.name)
inline fun <reified T : Serializable> Intent.getSerializableExtra() = getSerializableExtra(T::class.java.name) as T?
fun Intent.getStringExtra() = getStringExtra(String::class.java.name)

inline fun <reified T : Activity> Context.startActivity(vararg params: Any) {
    val arrayOfPairs = params.map { Pair(it.javaClass.name, it) }.toTypedArray()
    AnkoInternals.internalStartActivity(this, T::class.java, arrayOfPairs)
}

inline fun <reified T : Activity> Activity.startActivityForResult(vararg params: Any) {
    val arrayOfPairs = params.map { Pair(it.javaClass.name, it) }.toTypedArray()
    AnkoInternals.internalStartActivityForResult(this, T::class.java, 0, arrayOfPairs)
}

inline fun <reified T : Activity> FragmentActivity.startActivityFromFragment(fragment: Fragment, vararg params: Any) {
    val arrayOfPairs = params.map { Pair(it.javaClass.name, it) }.toTypedArray()
    val intent = AnkoInternals.createIntent(this, T::class.java, arrayOfPairs)
    startActivityFromFragment(fragment, intent, 0)
}
