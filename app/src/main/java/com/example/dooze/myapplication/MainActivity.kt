package com.example.dooze.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.sundayfun.daycam.story.view.PlayerPaginationView

class MainActivity : AppCompatActivity() {

    private lateinit var sizeEt: EditText
    private lateinit var indexEt: EditText
    private lateinit var pagenationView: PlayerPaginationView
    private lateinit var pagerView: PlayerPagenationView
    private lateinit var rv: RecyclerView

    private var index = 0
    private var size = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sizeEt = findViewById(R.id.size_et)
        indexEt = findViewById(R.id.index_et)
        pagenationView = findViewById(R.id.pagenation)
        pagerView = findViewById(R.id.pagenation1)
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
            pagerView.updateIndex(index)
        }
    }

    fun next(view: View) {
        if (index < size) {
            index++
            pagenationView.updateIndex(index)
            pagerView.updateIndex(index)
        }
    }

    fun done(view: View) {
        size = sizeEt.text.toString().toInt()
        index = indexEt.text.toString().toInt()
        pagenationView.setPagerSize(size)
        pagenationView.updateIndex(index)
        pagerView.setPagerSize(size)
        pagerView.updateIndex(index)
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

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

    }

    class PagerItem : View {
        constructor(context: Context) : super(context)
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

        var scale = 1f
        private val paint = Paint()

        var normalItemWidth = ViewUtils.dp2px(6f, context.resources)
        var normalItemHeight = ViewUtils.dp2px(10f, context.resources)
        var radius = ViewUtils.dp2px(2f, context.resources)
        private val bounds = RectF(0f, 0f, 0f, 0f)

        init {
            paint.isAntiAlias = true
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            setMeasuredDimension(normalItemHeight, normalItemHeight)
        }

        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            val w = PlayerPaginationView.normalItemWidth * scale
            val h = PlayerPaginationView.normalItemHeight * scale
            bounds.set(
                (PlayerPaginationView.normalItemHeight - w) / 2,
                (PlayerPaginationView.normalItemHeight - h) / 2,
                (PlayerPaginationView.normalItemHeight + w) / 2,
                (PlayerPaginationView.normalItemHeight + h) / 2
            )
            paint.color = if (scale == 1f) Color.WHITE else Color.parseColor("#66FFFFFF")
            canvas?.drawRoundRect(bounds, radius.toFloat(), PlayerPaginationView.radius.toFloat(), paint)
        }
    }
}
