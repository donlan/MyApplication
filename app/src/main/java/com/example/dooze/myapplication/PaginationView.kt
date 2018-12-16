package com.sundayfun.daycam.story.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.dooze.myapplication.PlayerPagenationView
import com.example.dooze.myapplication.ViewUtils

/**
 * @author: dooze
 * @date: 2018/12/14 21:55
 * @lastModifyUser: dooze
 * @lastModifyDate: 2018/12/14 21:55
 * @description:
 */
class PlayerPaginationView : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context,pageSize: Int) : super(context){
        size = pageSize
        currentIndex = 0
    }
    companion object {
        //普通状态的指示颜色
        var normalColor = Color.parseColor("#66FFFFFF")
        //选中状态的颜色
        var focusColor = Color.WHITE
        //正常状态下一个item的宽度
        var normalItemWidth = 0
        //正常状态下一个item的高度
        var normalItemHeight = 0
        // item 的圆角半径
        var radius = 0
        //当前索引移动方向，true 向右移动，false向左移动
        var isPlus = true
    }

    //外部实际页数
    private var size = 2
    //对应外部的当前页
    private var currentIndex = 0
    //对应外部的当前页的位置 item指示所在的索引
    private var itemIndex = 2
    //用于显示指示索引的Item集合，size = 7
    private val items = mutableListOf<Item>()
    private val paint = Paint()
    //正常情况下从左至右每个item
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
        setPagerSize(9)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(normalItemHeight * 7, normalItemHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //分页数小于2的时候不显示
        if (size >= 2) {
            startDraw(canvas)
        }
    }

    private fun startDraw(canvas: Canvas?) {
        setupItems()
        for (i in 0 until items.size) {
            items[i].draw(canvas, paint)
            canvas?.translate((PlayerPagenationView.normalItemHeight).toFloat(), 0f)
        }
    }

    /**
     * 重置分页数
     */
    fun setPagerSize(pageSize: Int) {
        size = pageSize
        currentIndex = 0
        //最右边可用于小时item的索引
        endIndex = if (size < 5) 1 + size else 6
        //能显示正常大小item的最右索引
        rightIndex = if (size < 3) 1 + size else 4
    }

    private fun setupItems() {
        //剩余未指示的分页数
        val rightCount = size - currentIndex
        //前三页的情况，动画效果不一样这里单独设置
        if (currentIndex < 3) {
            itemIndex = currentIndex + 2
            //前两个不显示
            for (i in 0..1) {
                items[i].update(0f, false)
            }
            //正常大小
            for (i in 2..rightIndex) {
                items[i].update(1f, i == itemIndex)
            }
            //依次缩放的item
            for (i in rightIndex + 1..endIndex) {
                items[i].update(scaleArray[5 + (i - rightIndex - 1)], false)
            }
            //末尾不显示的item
            for (i in endIndex + 1 until items.size) {
                items[i].update(0f, false)
            }
        } else {
            itemIndex = 4
            //当前指示位置之前的item
            for (i in itemIndex downTo 0) {
                items[i].update(getScale(scaleArray[i]), false)
            }
            //最右边可以显示指示item的索引
            val r = if (rightCount <= 2) itemIndex + rightCount else items.size - 1
            for (i in itemIndex + 1..r) {
                items[i].update(getScale(scaleArray[5 + i - itemIndex - 1]), false)
            }
            //剩余不需要显示的item
            for (i in r + 1 until items.size) {
                items[i].update()
            }
        }
        //正反方向移动时动画发生的效果不一样
        if ((isPlus && currentIndex > 2) || (!isPlus && currentIndex >= 2)) {
            for (i in 0..6) {
                items[i].fraction = curScaleFraction
            }
        }
        items[itemIndex].isFocus = true
        checkNoSee(itemIndex)
    }

    /**
     * 检查所有不应该显示的指示item，并设置其scale值为0
     */
    private fun checkNoSee(itemIndex: Int) {
        //第一个item应该显示的索引
        val start = itemIndex - currentIndex
        //最后一个显示item的索引
        val end = itemIndex + (size - currentIndex)
        for (i in 0 until start) {
            items[i].scale = 0f
        }
        for (i in end until items.size) {
            items[i].scale = 0f
        }
    }

    private fun getScale(scale: Float): Float {
        return scale
    }


    private var curScaleFraction = 1f

    /**
     *更新当前指示的页索引
     */
    fun updateIndex(index: Int) {
        if (index < 0 || index >= size || currentIndex == index) {
            return
        }
        isPlus = index > currentIndex
        currentIndex = index
        //通过ValueAnimator改变缩放比例进而实现滚动动画效果
        val va = ValueAnimator.ofFloat(0f, 1f)
        va.duration = 300
        va.addUpdateListener {
            curScaleFraction = it.animatedFraction
            invalidate()
        }
        va.start()
    }

    /**
     * 指示器的每个item的数据封装类
     */
    private class Item(var scale: Float = 1f, var isFocus: Boolean = false) {
        private val bounds = RectF(0f, 0f, 0f, 0f)
        //动画执行百分比分数,范围 0..1
        var fraction = 1f

        /**
         * 更新item的显示参数，缺省参数时不显示时的参数
         */
        fun update(newScale: Float = 0f, newIsFocus: Boolean = false) {
            scale = newScale
            isFocus = newIsFocus
        }

        /**
         * 通过fraction计算x轴的偏移（配合ValueAnimator可产生滚动效果）
         */
        private fun getDx(distance: Float): Float {
            return if (isPlus) {
                distance + normalItemWidth - normalItemWidth * fraction
            } else {
                distance - normalItemWidth + normalItemWidth * fraction
            }

        }

        /**
         * 根据scale值计算当前item的绘制区域
         */
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
            paint.color = if (isFocus) focusColor else normalColor
            canvas?.drawRoundRect(bounds, radius.toFloat(), radius.toFloat(), paint)
        }
    }
}