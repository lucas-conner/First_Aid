import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pa4.FirstAidTopic // Update with your actual package name

class FirstAidAdapter(private var topicsList: List<FirstAidTopic>) : RecyclerView.Adapter<FirstAidAdapter.ViewHolder>() {
    var isGrouped = false

    class ViewHolder(itemView: TextView) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topic = topicsList[position]
        holder.textView.text = topic.name
    }

    override fun getItemCount(): Int = topicsList.size

    fun updateList(newList: List<FirstAidTopic>) {
        topicsList = newList
        notifyDataSetChanged()
    }
}
