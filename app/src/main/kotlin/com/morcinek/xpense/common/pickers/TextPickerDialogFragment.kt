package com.morcinek.xpense.common.pickers

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.morcinek.xpense.R
import com.morcinek.xpense.common.adapter.AbstractRecyclerViewAdapter
import com.morcinek.xpense.common.adapter.AbstractRecyclerViewAdapter.OnItemClickListener
import kotlinx.android.synthetic.main.text_picker.*
import org.jetbrains.anko.textChangedListener

/**
 * Copyright 2016 Tomasz Morcinek. All rights reserved.
 */
abstract class TextPickerDialogFragment<T : Any> : DialogFragment(), OnItemClickListener<T> {

    protected abstract val adapter: AbstractRecyclerViewAdapter<T, out RecyclerView.ViewHolder>

    open protected fun extractString(item: T): String = item.toString()

    var  onItemSetListener: ((Any) -> Unit)? = null

    lateinit var items: List<T>

    var selectedItem: T? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.text_picker, container)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, 0)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupRecyclerView()
        setupEditText()
    }

    protected fun setTitle(title: Int) {
        titleView.setText(title)
    }

    private fun setupAdapter() {
        adapter.itemClickListener = this
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    private fun setupEditText() {
        editText.textChangedListener {
            onTextChanged { charSequence, start, before, count ->
                adapter.setList(items.filter { extractString(it).contains(charSequence!!, true) })
            }
        }
    }
}
