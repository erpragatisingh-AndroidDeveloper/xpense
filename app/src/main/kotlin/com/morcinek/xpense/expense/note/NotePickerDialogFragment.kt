package com.morcinek.xpense.expense.note

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.morcinek.xpense.Application
import com.morcinek.xpense.R
import com.morcinek.xpense.common.adapter.AbstractRecyclerViewAdapter
import com.morcinek.xpense.common.pickers.TextPickerDialogFragment
import com.morcinek.xpense.common.utils.getTrimString
import com.morcinek.xpense.common.utils.setTextWithSelection
import com.morcinek.xpense.common.utils.showSoftInput
import com.morcinek.xpense.data.note.NoteManager
import kotlinx.android.synthetic.main.text_picker.*
import javax.inject.Inject

/**
 * Copyright 2016 Tomasz Morcinek. All rights reserved.
 */
class NotePickerDialogFragment : TextPickerDialogFragment<String>() {

    @Inject
    lateinit var noteManager: NoteManager

    override val adapter: AbstractRecyclerViewAdapter<String, out RecyclerView.ViewHolder> by lazy {
        NoteAdapter(context)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.application as Application).component.inject(this)

        setTitle(R.string.title_note)
        setupItems()
        setupButton()
        setupEditText()
        setupRecyclerView()
    }

    private fun setupItems() {
        items = noteManager.getNotes()
        adapter.setList(items)
    }

    private fun setupButton() {
        button.setImageResource(R.drawable.ic_action_accept)
        button.setOnClickListener({
            val text = editText.getTrimString()
            onItemSetListener?.invoke(text)
            dialog.dismiss()
        })
    }

    private fun setupEditText() {
        dialog.window.showSoftInput()
        editText.setTextWithSelection(selectedItem!!)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
    }

    override fun onItemClicked(item: String) {
        editText.setTextWithSelection(item.toString())
    }
}
