package dista.learning.marqueetext

import android.net.IpSecManager.ResourceUnavailableException
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {
    private lateinit var scrollView: MarqueeLayout
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        scrollView = findViewById(R.id.horizontalMarquee)
        val sections = listOf(
            "Hello1",
            "Hello1",
            "Hello1",
            "Hello1",
            "Hello1",
            "Hello1",
            "Hello1",
            "Hello1",
            "Hello1"
        )
        for(section in sections){
            val textView1 = addTextView(section)
            scrollView.addTextViewSection(textView1)
        }
        // add the textView to it

        handler = Handler()
       runnable = object :Runnable{
           override fun run() {
               scrollView.smoothScrollBy(2, 0) // here i can adjust the speed of horizontal scroll
                       if(!scrollView.canScrollHorizontally(1)) {
                           scrollView.scrollTo(0,0)
                       }
               handler.postDelayed(this, 10)
           }
       }

    }

    private fun addTextView(text: String): TextView {
        val textView = TextView(this)
        textView.text = text
        val paddingInDp = 20 // Adjust this value as needed
        val density = resources.displayMetrics.density
        val paddingInPx = (paddingInDp * density).toInt()
        textView.setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx)

        return textView
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