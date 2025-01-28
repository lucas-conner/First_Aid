package com.example.pa4

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException


class MedicalTermAdapter(
    private var terms: List<FirstAidInfo>,
    private val onItemClick: (Int) -> Unit, // Expects an Int
    private val resourceMap: Map<String, String>
) : RecyclerView.Adapter<MedicalTermAdapter.TermViewHolder>() {

    inner class TermViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val termTextView: TextView = itemView.findViewById(R.id.medical_term_text_view)
        // Add other views here if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medical_term, parent, false)
        return TermViewHolder(view)
    }

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        val currentTerm = terms[position]
        holder.termTextView.text = resourceMap[currentTerm.termKey] ?: "Missing Text"
        holder.itemView.setOnClickListener { onItemClick(currentTerm.id) } // Passes the ID as Int
    }


    override fun getItemCount(): Int {
        return terms.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTerms: List<FirstAidInfo>) {
        terms = newTerms
        notifyDataSetChanged()
    }

    companion object {
        suspend fun loadTermsFromJson(context: Context): List<FirstAidInfo> {
            return withContext(Dispatchers.IO) {
                try {
                    val jsonString = try {
                        // Read the JSON file from assets
                        val inputStream = context.assets.open("first_aid_topics.json")
                        val size = inputStream.available()
                        val buffer = ByteArray(size)
                        inputStream.read(buffer)
                        inputStream.close()
                        String(buffer)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        return@withContext emptyList<FirstAidInfo>()
                    }
                    // Parse JSON using Gson
                    val type = object : TypeToken<List<FirstAidInfo>>() {}.type
                    Gson().fromJson(jsonString, type)
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList()
                }
            }
        }
    }
}
