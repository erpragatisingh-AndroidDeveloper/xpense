package com.morcinek.xpense.project

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.morcinek.xpense.R
import com.morcinek.xpense.common.adapter.AbstractRecyclerViewAdapter
import com.morcinek.xpense.common.pickers.TextPickerDialogFragment
import com.morcinek.xpense.common.utils.getTrimString
import com.morcinek.xpense.common.utils.hideSoftInput
import com.morcinek.xpense.expense.note.NoteAdapter
import kotlinx.android.synthetic.main.text_picker.*
import java.util.*

/**
 * Copyright 2016 Tomasz Morcinek. All rights reserved.
 */
class CurrencyPickerDialogFragment : TextPickerDialogFragment<String>() {

    override val adapter: AbstractRecyclerViewAdapter<out Any, out RecyclerView.ViewHolder> by lazy {
        NoteAdapter(context)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(R.string.title_currency)
        setupItems()
        setupButton()
    }

    private fun setupItems() {
        items = Currency.getAvailableCurrencies().map { it.symbol }
        adapter.setList(items)
    }

    private fun setupButton() {
        isButtonVisible = { it.isNotBlank() && it.length <= 3 }
        button.isEnabled = false
        button.setImageResource(R.drawable.ic_action_accept)
        button.setOnClickListener({
            onItemClicked(editText.getTrimString())
        })
    }

    override fun onItemClicked(item: String) {
        onItemSetListener?.invoke(item)
        activity.hideSoftInput(editText)
        dialog.dismiss()
    }
}