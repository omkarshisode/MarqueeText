package dista.learning.marqueetext

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.widget.TextView

class MainActivity4 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        supportActionBar?.title = "Activity 4"
        val marqueeTextView:TextView = findViewById(R.id.marqueeTextView)


        val boldText = createClickableSpan("Bold------------------", MainActivity::class.java)
        val underlinedText = createClickableSpan(" Underlined---------------", MainActivity2::class.java)
        val coloredText = createClickableSpan(" Color------------------------", MainActivity3::class.java)

        val finalText = SpannableStringBuilder().append(boldText).append(underlinedText).append(coloredText)

        marqueeTextView.text = finalText
        marqueeTextView.movementMethod = LinkMovementMethod.getInstance()
        marqueeTextView.gravity = Gravity.CENTER_VERTICAL  // Adjust gravity as needed
    }

    private fun createClickableSpan(text: String, destinationActivity: Class<out AppCompatActivity>): SpannableString {

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: android.view.View) {
                val intent = Intent(this@MainActivity4, destinationActivity)
                startActivity(intent)
            }
        }

        val spannableString = SpannableString(text)
        spannableString.setSpan(clickableSpan, 0, text.length, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)

        return spannableString
    }
}