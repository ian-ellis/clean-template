package com.github.ianellis.clean.presentation.utils.recyclerview.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import com.github.ianellis.clean.presentation.R

open class DividerDecoration(
    private val context: Context,
    private val height: Int? = null,
    @ColorRes
    private val color: Int? = null,
    private val insetLeft: Int = 0,
    private val insetRight: Int = 0
) : RecyclerView.ItemDecoration() {

    private val dividerHeight: Int by lazy {
        height ?: context.resources.getDimensionPixelSize(R.dimen.divider_height)
    }

    private val dividerColor: Int by lazy {
        context.resources.getColor(color ?: R.color.divider_color)
    }

    protected open fun shouldShowDivider(index: Int, count: Int): Boolean {
        return (0 until count).contains(index)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = parent.getChildAdapterPosition(view)
        if (shouldShowDivider(index, state.itemCount)) {
            outRect.bottom += dividerHeight
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(child)
            if (index >= 0 && shouldShowDivider(index, parent.adapter!!.itemCount)) {
                draw(c, child, insetLeft, insetRight, dividerHeight.toFloat())
            }
        }
    }

    private fun draw(c: Canvas, child: View, left: Int, right: Int, dividerHeight: Float) {
        val params = child.layoutParams as RecyclerView.LayoutParams
        val dividerRect = Rect()
        dividerRect.top = child.bottom + params.bottomMargin
        dividerRect.bottom = (dividerRect.top + dividerHeight).toInt()
        dividerRect.left = left
        dividerRect.right = right

        val paint = Paint()
        paint.color = context.resources.getColor(dividerColor, context.theme)
        c.drawRect(dividerRect, paint)
    }
}
