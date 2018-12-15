package com.example.dooze.myapplication

import android.content.Context
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.sundayfun.daycam.story.view.PlayerPagenationView

class MainActivity : AppCompatActivity() {

    private lateinit var sizeEt: EditText
    private lateinit var indexEt: EditText
    private lateinit var pagenationView: PlayerPagenationView
    private lateinit var rv: RecyclerView

    private var index = 0
    private var size = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sizeEt = findViewById(R.id.size_et)
        indexEt = findViewById(R.id.index_et)
        pagenationView = findViewById(R.id.pagenation)
//        rv = findViewById(R.id.rv)
//        rv.layoutManager = LinearLayoutManager(this)
//        LinearSnapHelper().attachToRecyclerView(rv)
//        rv.addItemDecoration(object : RecyclerView.ItemDecoration() {
//            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//               val llm =  parent.layoutManager as LinearLayoutManager
//            }
//        })
//        rv.adapter = Adapter()

    }

    fun pre(view: View) {
        if (index > 0) {
            index--
            pagenationView.updateIndex(index)
        }
    }

    fun next(view: View) {
        if (index < size ) {
            index++
            pagenationView.updateIndex(index)
        }
    }

    fun done(view: View) {
        size = sizeEt.text.toString().toInt()
        index = indexEt.text.toString().toInt()
        pagenationView.setPagerSize(size)
        pagenationView.updateIndex(index)
    }


    class Adapter() : RecyclerView.Adapter<Holder>() {


        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
            return Holder(PagerItem(p0.context))
        }

        override fun getItemCount(): Int {
            return 12
        }

        override fun onBindViewHolder(p0: Holder, p1: Int) {

        }

    }

    class Holder(view: View): RecyclerView.ViewHolder(view) {

    }

    class PagerItem : View {
        constructor(context: Context) : super(context)
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

        var scale = 1f
        private val paint = Paint()

        var normalItemWidth = ViewUtils.dp2px(6f, context.resources)
        var normalItemHeight = ViewUtils.dp2px(10f, context.resources)
        var radius = ViewUtils.dp2px(2f, context.resources)
        private val bounds = RectF(0f,0f,0f,0f)

        init {
            paint.isAntiAlias = true
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            setMeasuredDimension(normalItemHeight, normalItemHeight)
        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            val w = PlayerPagenationView.normalItemWidth * scale
            val h = PlayerPagenationView.normalItemHeight * scale
            bounds.set(
                (PlayerPagenationView.normalItemHeight - w) / 2,
                (PlayerPagenationView.normalItemHeight - h) / 2,
                (PlayerPagenationView.normalItemHeight + w) / 2,
                (PlayerPagenationView.normalItemHeight + h) / 2
            )
            paint.color = if (scale == 1f) Color.WHITE else Color.parseColor("#66FFFFFF")
            canvas?.drawRoundRect(bounds, radius.toFloat(), PlayerPagenationView.radius.toFloat(), paint)
        }
    }
}
