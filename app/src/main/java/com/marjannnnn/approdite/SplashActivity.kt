package com.marjannnnn.approdite

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var handler: Handler? = null
    private lateinit var titleSplash: TextView

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        titleSplash = findViewById(R.id.title_splash)

        val titlePart1 = "Digital\nplatform\nfor managing\n"
        val titlePart2 = "project tasks."

        val spannableString = SpannableString(titlePart1 + titlePart2)
        spannableString.setSpan(
            ForegroundColorSpan(getColor(R.color.white)),
            0,
            titlePart1.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(getColor(R.color.purple_20)),
            titlePart1.length,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        titleSplash.text = spannableString

        handler = Handler(Looper.getMainLooper())
        handler!!.postDelayed({
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}