package com.example.pa4

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var resourceMap: Map<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Initialize the resource map here if it's not initialized elsewhere
        resourceMap = preloadResources()

        val termId = intent.getIntExtra("TERM_ID", -1)
        if (termId != -1) {
            loadFirstAidInfo(termId)
        } else {
            Toast.makeText(this, "Invalid term ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.back_button).setOnClickListener {
            finish() // Closes the current activity and returns to the previous one.
        }

        setDisclaimerText()  // Call to set the disclaimer text at the bottom or any appropriate place in the activity
    }

    private fun preloadResources(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val fields = R.string::class.java.fields
        for (field in fields) {
            try {
                val resourceId = field.getInt(null)
                val resourceName = field.name
                map[resourceName] = getString(resourceId)
                Log.d("ResourceLoad", "Loaded: $resourceName = ${getString(resourceId)}")
            } catch (e: IllegalAccessException) {
                Log.e("ResourceLoad", "Failed to access string resource", e)
            }
        }
        return map
    }

    private fun loadFirstAidInfo(termId: Int) {
        val database = AppDatabase.getDatabase(applicationContext)
        val firstAidInfoDao = database.firstAidInfoDao()

        lifecycleScope.launch {
            try {
                val info = firstAidInfoDao.getInfoById(termId)
                if (info != null) {
                    Log.d("DataFetch", "Data fetched successfully: $info")
                    displayFirstAidInfo(info)
                } else {
                    Log.d("DataFetch", "No data found for term ID: $termId")
                    Toast.makeText(this@DetailActivity, "No first aid info found for the provided term ID.", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("DataFetch", "Error fetching first aid info", e)
                Toast.makeText(this@DetailActivity, "Failed to fetch first aid info", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayFirstAidInfo(firstAidInfo: FirstAidInfo) {
        // Log to trace the data received
        Log.d("DisplayInfo", "Received FirstAidInfo: $firstAidInfo")

        // Fetch and set the term
        val termTextView = findViewById<TextView>(R.id.term_text_view)
        val termText = firstAidInfo.termKey?.let { getStringResourceByKey(it) }
        termTextView.text = termText
        Log.d("DisplayInfo", "Term text set as: $termText")

        // Fetch and set the overview, applying bullet formatting if applicable
        val overviewTextView = findViewById<TextView>(R.id.overview_text_view)
        val overviewText = firstAidInfo.overviewKey?.let { getStringResourceByKey(it) }
        overviewTextView.text = overviewText?.let { formatTextWithBullets(it) }
        Log.d("DisplayInfo", "Overview text set with bullets: $overviewText")

        // Apply the bullet formatting and adjust visibility for all relevant TextViews
        firstAidInfo.actionKey?.let { applyFormattingAndVisibility(it, R.id.action_text_view, R.id.label_action) }
        firstAidInfo.symptomsKey?.let { applyFormattingAndVisibility(it, R.id.symptoms_text_view, R.id.label_symptoms) }
        firstAidInfo.triggersKey?.let { applyFormattingAndVisibility(it, R.id.triggers_text_view, R.id.label_triggers) }
        firstAidInfo.treatmentKey?.let { applyFormattingAndVisibility(it, R.id.treatment_text_view, R.id.label_treatment) }
        firstAidInfo.preventionKey?.let { applyFormattingAndVisibility(it, R.id.prevention_text_view, R.id.label_prevention) }

        // Set images conditionally based on certain terms
        firstAidInfo.termKey?.let { setImagesBasedOnTerm(it) }
    }

    private fun applyFormattingAndVisibility(key: String, textViewId: Int, headerId: Int) {
        val textView = findViewById<TextView>(textViewId)
        val content = getStringResourceByKey(key)
        val formattedContent = formatTextWithBullets(content)
        textView.text = formattedContent
        adjustTextViewVisibility(headerId, textViewId, formattedContent)
        Log.d("DisplayInfo", "Formatted and set visibility for $key: $formattedContent")
    }

    private fun getStringResourceByKey(key: String): String {
        val result = resourceMap[key] ?: "Missing Text"
        Log.d("ResourceLoad", "Key requested: $key, Result: $result")
        return result
    }


    private fun setImagesBasedOnTerm(termKey: String) {
        val imageView1 = findViewById<ImageView>(R.id.detail_image_view1)
        val imageView2 = findViewById<ImageView>(R.id.detail_image_view2)

        when (getStringResourceByKey(termKey)) {
            getString(R.string.ForeignObjectSwallowed_term),
            getString(R.string.ForeignObjectInhaled_term)
            -> {
                imageView1.setImageResource(R.drawable.choking_five_five)
                imageView2.setImageResource(R.drawable.self_heimlich)
                imageView1.visibility = View.VISIBLE
                imageView2.visibility = View.VISIBLE
            }

            getString(R.string.SpiderBites_term) -> {
                imageView1.setImageResource(R.drawable.brown_recluse)
                imageView2.setImageResource(R.drawable.black_widow)
                imageView1.visibility = View.VISIBLE
                imageView2.visibility = View.VISIBLE
            }

            else -> {
                imageView1.visibility = View.GONE
                imageView2.visibility = View.GONE
            }
        }
    }










    private fun formatTextWithBullets(text: String): CharSequence {
        // Splitting the text into lines, prepending each with a bullet, and then joining back to a single string
        val bulletedList = text.split("\n").joinToString(separator = "<br>• ", prefix = "• ") { Html.escapeHtml(it) }
        return Html.fromHtml(bulletedList, Html.FROM_HTML_MODE_COMPACT)
    }



    private fun setDisclaimerText() {
        val textView = findViewById<TextView>(R.id.disclaimer_text_view)
        val fullText = getString(R.string.disclaimer_text)
        val spannableString = SpannableString(fullText)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Open URL in a web browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mayoclinic.org/first-aid"))
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }
        }

        val startIndex = fullText.indexOf("Mayo Clinic\'s official website")
        val endIndex = startIndex + "Mayo Clinic\'s official website".length
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance() // This makes the link clickable
        textView.highlightColor = Color.TRANSPARENT // Optional: removes the default link highlight
    }


    private fun adjustTextViewVisibility(headerId: Int, contentId: Int, content: CharSequence) {
        val header = findViewById<TextView>(headerId)
        val textView = findViewById<TextView>(contentId)
        // Check if content is empty or effectively empty
        if (content.isBlank() || content == "•" || content.trim().isEmpty()) {
            header.visibility = View.GONE
            textView.visibility = View.GONE
        } else {
            header.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
            textView.text = content
        }
    }

}