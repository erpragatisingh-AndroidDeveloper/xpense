package com.morcinek.xpense.home.category

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import com.morcinek.xpense.R
import com.morcinek.xpense.common.CreateActivity
import com.morcinek.xpense.common.adapter.AbstractRecyclerViewAdapter
import com.morcinek.xpense.common.utils.getParcelable
import com.morcinek.xpense.common.utils.getTrimString
import com.morcinek.xpense.common.utils.setTextWithSelection
import com.morcinek.xpense.data.category.Category
import kotlinx.android.synthetic.main.category.*

/**
 * Copyright 2016 Tomasz Morcinek. All rights reserved.
 */
class CategoryActivity : CreateActivity<Category>(), AbstractRecyclerViewAdapter.OnItemClickListener<Int> {

    private val list = listOf(
            0xFF000000, 0xFFffffff, 0xFFff0000, 0xFFffc0cb, 0xFF008080, 0xFF0000ff, 0xFFeeeeee,
            0xFF40e0d0, 0xFFcccccc, 0xFFffd700, 0xFF00ffff, 0xFFff7373, 0xFFc0c0c0, 0xFF333333
    ).map { it.toInt() }


    private lateinit var colorAdapter: ColorAdapter

    override var item: Category
        get() = Category(editText.getTrimString(), 0)
        set(value) {
            editText.setTextWithSelection(value.name)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category)

        setupAdapter()
        setupRecyclerView()
    }

    override fun restoreItem(bundle: Bundle) = bundle.getParcelable<Category>()

    private fun setupAdapter() {
        colorAdapter = ColorAdapter(this)
        colorAdapter.itemClickListener = this
        colorAdapter.setList(list)
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = colorAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 5)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun onItemClicked(item: Int) {

    }

    override fun onDoneItemSelected() {
        TODO()
    }
}