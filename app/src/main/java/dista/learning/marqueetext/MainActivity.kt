package dista.learning.marqueetext

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.text.method.Touch.scrollTo
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginLeft
import androidx.core.view.postDelayed
import org.w3c.dom.Text
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var marqueeLayout: MarqueeLayout
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        handler = Handler()

         marqueeLayout = findViewById(R.id.marquee_layout)
        val sections = listOf(
            MarqueeSection("Activity 1 ", MainActivity2::class.java),
            MarqueeSection("Activity 2 ",  MainActivity3::class.java),
            MarqueeSection("Activity 3 ",  MainActivity2::class.java),
            MarqueeSection("Activity 4 ", MainActivity3::class.java),
            MarqueeSection("Activity 5 ", MainActivity2::class.java),
            MarqueeSection("Activity 6 ", MainActivity3::class.java),
            MarqueeSection("Activity 7 ", MainActivity3::class.java),
            MarqueeSection("Activity 8 ", MainActivity3::class.java),
            // Add more sections as needed
        )

        for (section in sections) {
            val sectionView = createMarqueeTextView(section)
            marqueeLayout.addTextViewSection(sectionView)
        }
//
//        marqueeLayout.setDuration(20000)
//        marqueeLayout.startAnimation()
//        marqueeLayout.startMarqueeAnimation()
//        marqueeLayout.setDurationLinear(16000)
        marqueeLayout.overScrollMode = View.OVER_SCROLL_NEVER
        marqueeLayout.isHorizontalScrollBarEnabled = false

//        // Setting Padding
//        val horizontalPaddingInPixels = resources.getDimensionPixelSize(androidx.appcompat.R.dimen.tooltip_horizontal_padding)
//        marqueeLayout.setPadding(horizontalPaddingInPixels, 0, horizontalPaddingInPixels, 0)

// OR Setting Margin (requires parent layout to have ViewGroup.LayoutParams)
//        val horizontalMarginInPixels =
//        val params = marqueeLayout.layoutParams as ViewGroup.MarginLayoutParams
//        params.leftMargin = 50
//        params.rightMargin =
//        marqueeLayout.layoutParams = params

        marqueeLayout.post {
            // Set the initial scroll position to the rightmost end smoothly
            marqueeLayout.fullScroll(View.FOCUS_RIGHT)
        }
        runnable = object:Runnable{
            override fun run() {
                marqueeLayout.smoothScrollBy(2,0)
                // If the scroll has reached the end, reset the scroll position
                if (!marqueeLayout.canScrollHorizontally(1)) {
                    marqueeLayout.scrollTo(0, 0)
//                    marqueeLayout.fullScroll(View.FOCUS_RIGHT)
                }

                handler.postDelayed(this, 16)
            }

        }


    }

    private fun createMarqueeTextView(section: MarqueeSection): TextView {
//        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val sectionView = inflater.inflate(R.layout.item_marquee, null) as LinearLayout

        val titleTextView = TextView(this)
//        val titleTextView  = sectionView.findViewById<TextView>(R.id.titleTextView1)
        titleTextView.text = section.title
        titleTextView.setTextColor(resources.getColor(R.color.black))
        titleTextView.setTypeface(null, Typeface.BOLD)
        titleTextView.isSelected = true
        titleTextView.ellipsize = TextUtils.TruncateAt.MARQUEE
        titleTextView.maxLines = 1 // to show the max line at time in the marquee text
        val paddingInDp = 20 // Adjust this value as needed
        val density = resources.displayMetrics.density
        val paddingInPx = (paddingInDp * density).toInt()
        titleTextView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)



        titleTextView.setOnClickListener {
            val intent = Intent(this, section.activityClass)
            startActivity(intent)
        }

        return titleTextView
    }

    override fun onResume() {
        super.onResume()
        // Start autoscroll when the activity is resumed
        handler.postDelayed(runnable, 0)
    }

    override fun onPause() {
        super.onPause()
        // Stop autoscroll when the activity is paused
        handler.removeCallbacks(runnable)
    }
}
data class MarqueeSection(
    val title: String,
    val activityClass: Class<*>
    )


class MarqueeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : HorizontalScrollView(context, attrs) {

    private val animation: Animation
    private val linearLayout: LinearLayout


    init {
        linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.gravity = Gravity.CENTER
        addView(linearLayout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, +1f,
            Animation.RELATIVE_TO_SELF, -1f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f
        )

        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.RESTART

    }

    fun addTextViewSection(textView: TextView) {
        linearLayout.addView(textView)
    }

    fun setDuration(durationMillis: Int) {
        animation.duration = durationMillis.toLong()
    }

    fun startAnimation() {
        startAnimation(animation)
    }

    // horizontal scroll view

}



