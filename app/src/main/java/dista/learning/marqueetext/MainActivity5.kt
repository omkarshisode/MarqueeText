package dista.learning.marqueetext

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.View.resolveSize
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.coroutines.NonCancellable.start

class MainActivity5 : AppCompatActivity() {
    private lateinit var marqueeLayout:MarqueeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)
        //TODO getting delay at the first and while starting the animation need to rector this
//TODO Refactor this code and then change it by you login you can minimize it more and more
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
        marqueeLayout.startMarqueeAnimation()
    }
}

class MarqueeLayout(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {

    private val marqueeTextViews: MutableList<TextView> = mutableListOf()


    init {
        // Add a layout change listener to listen for changes in the layout
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // Remove the layout change listener after it's triggered
                viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Start the marquee animation after layout changes have been applied
                startMarqueeAnimation()
            }
        })
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



    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // Layout each child TextView
        var left = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.layout(left, 0, left + child.measuredWidth, child.measuredHeight)
            left += child.measuredWidth
        }
    }

    private var totalWidth = 0
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Measure each child TextView
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
        }

        // Set the total width and height based on the sum of child TextView widths and heights
         totalWidth = (0 until childCount).sumOf { getChildAt(it).measuredWidth }
        val totalHeight = (0 until childCount).maxOfOrNull { getChildAt(it).measuredHeight } ?: 0

        setMeasuredDimension(
            resolveSize(totalWidth, widthMeasureSpec),
            resolveSize(totalHeight, heightMeasureSpec)
        )
    }

    private fun calculateEndingValue(): Float {
        val totalTextViewWidth = calculateTotalTextViewWidth()
        val layoutWidth = width
        return -((totalTextViewWidth - layoutWidth).coerceAtLeast(0)).toFloat()
    }

    private fun calculateTotalTextViewWidth(): Int {
        var totalWidth = 0
        for (textView in marqueeTextViews) {
            totalWidth += textView.width
        }
        return totalWidth
    }
    fun startMarqueeAnimation() {
        for (i in marqueeTextViews.indices) {
            val textView = marqueeTextViews[i]
            // Set initial translationX value to the width of the MarqueeLayout
            textView.translationX = width.toFloat() * (i + 1)
            val screenWidth = resources.displayMetrics.widthPixels.toFloat()

            val animator = ObjectAnimator.ofFloat(textView, "translationX", screenWidth, -totalWidth.toFloat())
            animator.duration = 21000 // Increase the duration for a slower, smoother animation
            animator.interpolator = LinearInterpolator()
            animator.repeatCount = ObjectAnimator.INFINITE
            animator.repeatMode = ObjectAnimator.RESTART

            animator.start()
        }
    }
}