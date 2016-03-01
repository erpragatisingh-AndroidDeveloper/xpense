package com.morcinek.xpense.home.statistics.charts

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import com.morcinek.xpense.Application
import com.morcinek.xpense.R
import com.morcinek.xpense.common.fragments.BaseFragment
import com.morcinek.xpense.common.pager.PagerAdapter
import com.morcinek.xpense.common.utils.*
import com.morcinek.xpense.data.category.Category
import com.morcinek.xpense.data.expense.Expense
import com.morcinek.xpense.data.expense.ExpenseManager
import com.morcinek.xpense.data.period.PeriodObjectFactory
import com.morcinek.xpense.home.category.CategoriesAdapter
import kotlinx.android.synthetic.main.days_charts.*
import lecho.lib.hellocharts.model.ColumnChartData
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.view.ColumnChartView
import lecho.lib.hellocharts.view.LineChartView
import org.jetbrains.anko.alert
import java.util.*
import javax.inject.Inject

/**
 * Copyright 2016 Tomasz Morcinek. All rights reserved.
 */
abstract class AbstractChartFragment : BaseFragment(), PagerAdapter.Page {

    abstract protected val filter: (Expense) -> Boolean

    abstract protected val chartDataGenerators: Map<Int, ChartDataGenerator>

    @Inject
    lateinit var expenseManager: ExpenseManager

    @Inject
    lateinit var periodObjectFactory: PeriodObjectFactory

    override fun getLayoutResourceId() = R.layout.days_charts

    protected val selectedCategories: ArrayList<Category> by lazy {
        val selectedCategories = arrayListOf<Category>()
        selectedCategories.addAll(defaultCategories())
        selectedCategories
    }

    private fun defaultCategories(): List<Category> {
        val categoriesExpenses = expenses().groupBy { it.category!! }
        return categoriesExpenses.map { it.key to it.value.sumByDouble { it.value } }.sortedByDescending { it.second }.map { it.first }
    }

    private fun expenses() = expenseManager.getExpenses().filter(filter)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity.application as Application).component.inject(this)

        setHasOptionsMenu(true)
        setupRecyclerView()
        generateChartData(expenses())
    }

    fun generateChartData(expenses: List<Expense>) {
        for (chart in listOf(columnChart, lineChart, categoriesChart)) {
            when (chart) {
                is LineChartView -> chart.lineChartData = chartDataGenerators.get(chart.id)!!.generateData(expenses, selectedCategories) as LineChartData
                is ColumnChartView -> chart.columnChartData = chartDataGenerators.get(chart.id)!!.generateData(expenses, selectedCategories) as ColumnChartData
            }
            chart.isZoomEnabled = false
        }
        updateCategoriesList()
    }

    private fun updateCategoriesList() {
        val adapter = recyclerView.adapter as CategoriesAdapter
        adapter.setList(selectedCategories)
        recyclerView.setLayoutHeight(adapter.itemCount * context.dimenSum(R.dimen.chart_category_item_height))
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = CategoriesChartAdapter(context)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        generateChartData(expenses())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.chart, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                showCategoriesDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCategoriesDialog() {
        val listView = createListView()
        context.alert() {
            customView(listView)
            positiveButton {
                if (listView.hasCheckedItems()) {
                    setupSelectedCategories(listView)
                    generateChartData(expenses())
                } else {
                    context.showSnackbar(view, R.string.no_categories_selected)
                }
            }
            negativeButton {}
        }.show()
    }

    private fun createListView(): ListView {
        val listView = ListView(context)
        listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
        listView.adapter = CategoriesFilterAdapter(context, R.layout.filter_category_item, defaultCategories())
        listView.forEachItemIndexed { index, item: Category -> setItemChecked(index, selectedCategories.contains(item)) }
        return listView
    }

    private fun setupSelectedCategories(listView: ListView) {
        selectedCategories.clear()
        selectedCategories.addAll(listView.getCheckedItems())
    }
}