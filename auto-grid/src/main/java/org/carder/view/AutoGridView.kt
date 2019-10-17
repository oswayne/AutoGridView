package org.carder.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import org.carder.sample.moment.R
import org.carder.view.adapter.AutoGridAdapter

/**
 * 网格化显示多张图片
 *
 * 当显示 1 张图时直接根据用户设置的图片宽高显示一张图片；
 * 多张图时根据用户自己设置的最大宽高显示(默认 3 * 3 的九宫格显示方式)，以九宫格为例：2-3 张图时则并列显示在一排，3-6 张时显为两排，
 * 6 张及以上时显示为九宫格，如果数量在 9 张以上，则最后一张为显示更多的按钮；
 *
 * @author Wayne Carder
 */
open class AutoGridView : ViewGroup {

    companion object {
        const val TAG = "AutoGridView"
        const val DEFAULT_CELL_SPACING = 10
        const val DEFAULT_MAX_COLUMN = 3
        const val DEFAULT_MAX_RAW = 3
    }

    /**
     * 实际单元格大小
     * 该值是更具子 View 的数量动态计算生成的
     */
    private var cellSize = 0
    /**
     * 单元格间隙
     * 用户可自主设置
     */
    private val cellSpacing: Int
    /**
     * 最大列数
     */
    private val maxColumn: Int
    /**
     * 最大行数
     */
    private val maxRaw: Int
    /**
     * 真实列数
     */
    private var realColumn = 1
    /**
     * 真实行数
     */
    private var realRaw = 1
    /**
     * 是否渲染过
     * 用于防止重复无谓的重复绘制，影响性能
     */
    private var hasAdapter = false

    private lateinit var childViewAdapter: AutoGridAdapter<*>

    private var childViewList = ArrayList<View>(DEFAULT_MAX_COLUMN * DEFAULT_MAX_RAW)

    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.AutoGridView
        )
        cellSpacing = typedArray
            .getInteger(
                R.styleable.AutoGridView_cellSpacing,
                DEFAULT_CELL_SPACING
            )
        maxColumn = typedArray
            .getInteger(
                R.styleable.AutoGridView_maxColumn,
                DEFAULT_MAX_COLUMN
            )
        maxRaw = typedArray
            .getInteger(
                R.styleable.AutoGridView_maxRaw,
                DEFAULT_MAX_RAW
            )
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        if (!hasAdapter) {
            Log.w(TAG, "No adapter, skip layout")
            setMeasuredDimension(width, height)
            return
        }

        measureChildren(widthMeasureSpec, heightMeasureSpec)
        if (childViewAdapter.count == 1) {
            // 单 View 模式
            cellSize = getChildAt(0).measuredWidth
            width = getChildAt(0).measuredWidth
            height = getChildAt(0).measuredHeight
        } else {
            // 多 View 模式
            cellSize =
                (width - paddingLeft - paddingRight - cellSpacing * (realColumn - 1)) / realColumn
            width = cellSize * realColumn + ((realColumn - 1) * cellSpacing)
            height = cellSize * realRaw + ((realRaw - 1) * cellSpacing) + paddingBottom + paddingTop
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (hasAdapter) {
            if (childCount == 1) {
                layoutSingleView()
            } else {
                layoutChildView()
            }
        }
    }

    private fun layoutSingleView() {
        val view = getChildAt(0)
        view.layout(
            paddingLeft,
            paddingTop,
            view.measuredWidth + paddingRight,
            view.measuredHeight + paddingBottom
        )
    }

    private fun layoutChildView() {
        for (raw in 0 until realRaw) {
            for (column in 0 until realColumn) {
                if (raw * realColumn + column < childViewList.size) {
                    childViewList[raw * realColumn + column].layout(
                        column * cellSpacing + column * cellSize,
                        raw * cellSpacing + raw * cellSize,
                        column * cellSpacing + (column + 1) * cellSize,
                        raw * cellSpacing + (raw + 1) * cellSize
                    )
                }
            }
        }
    }

    open fun setAdapter(adapter: AutoGridAdapter<*>) {
        childViewAdapter = adapter
        removeChildView()
        hasAdapter = if (this.childViewAdapter.count != 0) {
            addChildView()
            calcCellCount()
            true
        } else {
            false
        }
    }

    /**
     * 添加子 View
     * 1. 调用 Adapter 中的 createView 方法创建 View
     * 2. 如过 AutoGridView yz
     */
    private fun addChildView() {
        for (index in 0 until childViewAdapter.count) {
            val view = childViewAdapter.createView(context, index)
            if (onItemClickListener != null) {
                view.setOnClickListener {
                    onItemClickListener!!.onItemClick(it, index)
                }
            }
            if (onItemLongClickListener != null) {
                view.setOnLongClickListener {
                    return@setOnLongClickListener onItemLongClickListener!!.onItemLongClick(
                        it,
                        index
                    )
                }
            }
            childViewList.add(view)
            childViewAdapter.initView(childViewList[index], index)
            addView(childViewList[index])
        }
    }

    /**
     * 计算单元格数量
     * 计算完成之后会为 realColumn 和 realRaw 赋值，为后续的子 View 摆放提供依据
     */
    private fun calcCellCount() {
        val count = childViewList.size
        if (count < maxColumn) {
            realColumn = count
        } else {
            realColumn = maxColumn
            realRaw = if (count % maxColumn == 0) count / maxColumn else count % maxColumn + 1
        }

        if (count % realRaw == 0) {
            realColumn = realRaw
        }
    }

    /**
     * 移除当前存在的所有子 View
     * 通常在重新设置适配器后触发
     */
    private fun removeChildView() {
        if (childViewList.isNotEmpty()) {
            childViewList.clear()
        }
        removeAllViews()
    }

    /**
     * 设置 View 的点击监听
     */
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    /**
     * 设置 View 的长按监听
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        onItemLongClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, position: Int): Boolean
    }
}
