package dista.learning.marqueetext

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.text.style.TypefaceSpan
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView

class MainActivity5 : AppCompatActivity() {
    private lateinit var marqueeLayout:MarqueeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

         marqueeLayout = findViewById(R.id.marqueeLayout)
        val sections = listOf(
            MarqueeSection("Activity 1 ", MainActivity::class.java),
            MarqueeSection("Activity 2 ",  MainActivity2::class.java),
            MarqueeSection("Activity 3 ",  MainActivity3::class.java),
            MarqueeSection("Activity 4 ", MainActivity4::class.java),
            MarqueeSection("Activity 5 ", MainActivity::class.java),
            MarqueeSection("Activity 6 ", MainActivity2::class.java),
            MarqueeSection("Activity 7 ", MainActivity3::class.java),
            MarqueeSection("Activity 8 ", MainActivity4::class.java),
            MarqueeSection("Activity 9 ", MainActivity::class.java),
            MarqueeSection("Activity 10 ", MainActivity2::class.java),
            MarqueeSection("Activity 11 ", MainActivity3::class.java),
            MarqueeSection("Activity 12 ", MainActivity4::class.java),
            MarqueeSection("Activity 13 ", MainActivity3::class.java),
        )
        for(section in sections){
            marqueeLayout.addMarqueeTextView(section)
        }

// Add more TextViews as needed
        marqueeLayout.startMarqueeAnimation()
    }

//    override fun onPause() {
//        super.onPause()
//        marqueeLayout.stopMarqueeAnimation()
//    }
}

class MarqueeLayout(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {

    private val marqueeTextViews: MutableList<TextView> = mutableListOf()
    private var currentScroll: Float = 0f

    private val handler: Handler = Handler()
    private val marqueeRunnable: Runnable = object : Runnable {
        override fun run() {
            // Update the current scroll position
            currentScroll -= MARQUEE_SCROLL_SPEED

            // If the text has scrolled completely, reset the scroll position
            if (currentScroll <= -getMaxTextWidth()) {
                currentScroll = width.toFloat()
            }

            // Force a redraw to update the scroll position for each TextView
            marqueeTextViews.forEach { it.translationX = currentScroll }

            // Schedule the next iteration
            handler.postDelayed(this, MARQUEE_UPDATE_DELAY)
        }
    }

    init {
        // Extract attributes if available
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MarqueeLayout)
            // You can add custom attributes handling here if needed
            typedArray.recycle()
        }
        currentScroll = -getMaxTextWidth()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // Layout each child TextView
        var left = width - getMaxTextWidth().toInt()
        for (child in marqueeTextViews) {
            child.layout(left, 0, left + child.measuredWidth, child.measuredHeight)
            left += child.measuredWidth-400 // to reduce the space between the alternative text view
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Measure each child TextView
        marqueeTextViews.forEach { it.measure(widthMeasureSpec, heightMeasureSpec) }

        // Set the total width and height based on the sum of child TextView widths and heights
        val totalWidth = marqueeTextViews.sumBy { it.measuredWidth }
        val totalHeight = marqueeTextViews.maxOfOrNull { it.measuredHeight } ?: 0

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec)
        )
    }

    fun addMarqueeTextView(marqueeSection: MarqueeSection) {
        val textView = createMarqueeTextView(marqueeSection)
        marqueeTextViews.add(textView)
        addView(textView)
    }

    private fun createMarqueeTextView(marqueeSection: MarqueeSection): TextView {
        val textView = TextView(context)
        textView.text = marqueeSection.title
        textView.textSize = 18f
        textView.setTypeface(null, Typeface.BOLD)
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        textView.isSelected = true
        textView.maxLines = 1
        textView.paintFlags = textView.paintFlags or Paint.ANTI_ALIAS_FLAG

        textView.setOnClickListener {
            val intent = Intent(context, marqueeSection.activityClass)
            context.startActivity(intent)
        }

        return textView
    }

    fun startMarqueeAnimation() {
        handler.removeCallbacks(marqueeRunnable)
        handler.postDelayed(marqueeRunnable, MARQUEE_UPDATE_DELAY)
    }

    fun stopMarqueeAnimation() {
        handler.removeCallbacks(marqueeRunnable)
    }

    private fun getMaxTextWidth(): Float {
        return marqueeTextViews.sumBy { it.width }.toFloat()
    }

    companion object {
        private const val MARQUEE_SCROLL_SPEED = 3.0f
        private const val MARQUEE_UPDATE_DELAY = 16L  // Update every 16 milliseconds (roughly 60 FPS)
    }
}