package com.morcinek.xpense.common.pager

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Copyright 2016 Tomasz Morcinek. All rights reserved.
 */
abstract class PagerAdapter(protected val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    abstract val fragments: List<Fragment>

    init {
        fragmentManager.fragments?.clear()
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString((getItem(position) as Page).title)
    }

    interface Page {
        val title: Int
    }
}