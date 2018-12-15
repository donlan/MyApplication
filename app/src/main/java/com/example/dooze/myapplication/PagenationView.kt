package com.sundayfun.daycam.story.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.dooze.myapplication.ViewUtils

/**
 * @author: dooze
 * @date: 2018/12/14 21:55
 * @lastModifyUser: dooze
 * @lastModifyDate: 2018/12/14 21:55
 * @description:
 */
class PlayerPagenationView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    companion object {
        var normalItemWidth = 0
        var normalItemHeight = 0
        var radius = 0
        var size = 0
        var isPlus = true
    }


    private var currentIndex = 0
    private var itemIndex = 2
    private val items = mutableListOf<Item>()
    private val paint = Paint()
    private val scaleArray = floatArrayOf(0.3f, 0.7f, 1f, 1f, 1f, 0.7f, 0.3f)
    private var endIndex = 0
    private var rightIndex = 0

    init {

        for (i in 0..6) {
            items.add(Item())
        }
        paint.isAntiAlias = true
        normalItemWidth = ViewUtils.dp2px(6f, context.resources)
        normalItemHeight = ViewUtils.dp2px(10f, context.resources)
        radius = ViewUtils.dp2px(2f, context.resources)
        setPagerSize(6)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(normalItemHeight * 7, normalItemHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (size >= 2) {
            startDraw(canvas)
        }
    }

    private fun startDraw(canvas: Canvas?) {
        setupItems()
        for (i in 0 until items.size) {
            items[i].draw(canvas, paint)
            canvas?.translate((normalItemHeight).toFloat(), 0f)
        }
    }

    fun setPagerSize(pageSize: Int) {
        size = pageSize
        endIndex = if (size < 5) 1 + size else 6
        rightIndex = if (size < 3) 1 + size else 4

    }

    private fun setupItems() {
        val rightCount = size - currentIndex

        if (currentIndex < 3) {
            itemIndex = currentIndex + 2
            for (i in 0..1) {
                items[i].scale = 0f
                items[i].alpha = 0.5f
            }
            for (i in 2..rightIndex) {
                items[i].scale = 1f
                items[i].alpha = if (i == itemIndex) 1f else 0.5f
            }
            for (i in rightIndex + 1..endIndex) {
                items[i].alpha = 0.5f
                items[i].scale = scaleArray[5 + (i - rightIndex - 1)]
            }
            for (i in endIndex + 1 until items.size) {
                items[i].alpha = 0f
                items[i].scale = 0f
            }
        } else {
            itemIndex = 4
            for (i in itemIndex downTo 0) {
                items[i].alpha = 0f
                items[i].scale = getScale(scaleArray[i])
            }
            val r = if (rightCount <= 2) itemIndex + rightCount else items.size - 1
            for (i in itemIndex + 1..r) {
                items[i].alpha = 0f
                items[i].scale = getScale(scaleArray[5 + i - itemIndex - 1])
            }
            for (i in r + 1 until items.size) {
                items[i].alpha = 0f
                items[i].scale = 0f
            }
            Log.e("tag", "${currentIndex},${r},${rightIndex},${endIndex}")
        }
        if ((isPlus && currentIndex > 2) || (!isPlus && currentIndex >= 2))
            for (i in 0..6) {
                items[i].fraction = curScaleFrac
            }
        items[itemIndex].alpha = 1f
        checkNoSee(itemIndex)
    }

    private fun checkNoSee(itemIndex: Int) {
        var start = itemIndex - currentIndex
        var end = itemIndex + (size - currentIndex)
        for(i in 0 until start){
            items[i].scale = 0f
        }
        for(i in end until items.size){
            items[i].scale = 0f
        }
    }

    private fun getScale(scale: Float): Float {
        return scale
    }


    private var curScaleFrac = 1f
    fun updateIndex(index: Int) {
        if (index < 0 || index >= size || currentIndex == index) {
            return
        }
        isPlus = index > currentIndex
        currentIndex = index

        val va = ValueAnimator.ofFloat(0f, normalItemWidth.toFloat())
        va.duration = 300
        va.addUpdateListener {
            curScaleFrac = it.animatedFraction
            invalidate()
        }
        va.start()
    }

    private class Item(var scale: Float = 1f, var alpha: Float = 0.5f) {
        private val bounds = RectF(0f, 0f, 0f, 0f)
        var fraction = 1f

        private fun getDx(distance: Float): Float {
            return if (isPlus) {
                distance + normalItemWidth - normalItemWidth * fraction
            } else {
                distance - normalItemWidth + normalItemWidth * fraction
            }

        }

        private fun reset() {
            val w = normalItemWidth * scale
            val h = normalItemHeight * scale
            bounds.set(
                getDx((normalItemHeight - w) / 2),
                (normalItemHeight - h) / 2,
                getDx((normalItemHeight + w) / 2),
                (normalItemHeight + h) / 2
            )
        }


        fun draw(canvas: Canvas?, paint: Paint) {
            reset()
            paint.color = if (alpha == 1f) Color.WHITE else Color.parseColor("#66FFFFFF")
            canvas?.drawRoundRect(bounds, radius.toFloat(), radius.toFloat(), paint)

        }
    }
}