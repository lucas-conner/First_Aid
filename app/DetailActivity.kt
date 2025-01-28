import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val svedInstanceState = null
        super.onCreate(svedInstanceState)
        setContentView(R.layout.activity_detail)

        val termId = intent.getIntExtra("TERM_ID", 0)
        val term = DataRepository.medicalTerms.find { it.id == termId }

        findViewById<TextView>(R.id.definition_text_view).text =
            term?.definition ?: "Definition not found."
    }

    private fun setContentView(any: Any) {

    }
}
