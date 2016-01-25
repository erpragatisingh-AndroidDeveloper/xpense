package com.morcinek.xpense.data.expense

import android.os.Parcel
import android.os.Parcelable
import com.morcinek.xpense.common.utils.createParcel
import com.morcinek.xpense.common.utils.readCalendar
import com.morcinek.xpense.common.utils.readParcelable
import com.morcinek.xpense.common.utils.writeCalendar
import com.morcinek.xpense.data.category.Category
import java.util.*

/**
 * Copyright 2015 Tomasz Morcinek. All rights reserved.
 */
data class Expense(var value: Double = 0.0, var category: Category? = null, var note: String = "",
                   val date: Calendar = Calendar.getInstance()) : Comparable<Expense>, Parcelable {

    override fun compareTo(other: Expense) = (value - other.value + 0.5).toInt()

    operator fun plus(expense: Expense) = value + expense.value

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(value)
        dest.writeParcelable(category, 0)
        dest.writeString(note)
        dest.writeCalendar(date)
    }

    override fun describeContents() = 0

    companion object {
        val CREATOR = createParcel { Expense(it.readDouble(), it.readParcelable(Category.CREATOR), it.readString(), it.readCalendar()) }
    }
}