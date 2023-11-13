package ru.iandreyshev.jokesfeed.ui.feed

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.iandreyshev.jokesfeed.databinding.ItemJokeBinding
import ru.iandreyshev.jokesfeed.domain.Joke
import ru.iandreyshev.jokesfeed.ui.filter.name

class FeedAdapter : ListAdapter<Joke, FeedAdapter.FeedItemViewHolder>(Callback) {

    class FeedItemViewHolder(val binding: ItemJokeBinding) : RecyclerView.ViewHolder(binding.root)

    var highlightString = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder =
        LayoutInflater.from(parent.context)
            .let { ItemJokeBinding.inflate(it, parent, false) }
            .let { FeedItemViewHolder(it) }

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.date.text = "#${item.topic.name()}"

        if (highlightString.isNotBlank()) {
            highlightSubstring(holder.binding.text, item.text, highlightString)
        } else {
            holder.binding.text.text = item.text
        }
    }

    private fun highlightSubstring(textView: TextView, fullText: String, substring: String) {
        val spannableString = SpannableString(fullText)
        val startIndex = fullText.indexOf(substring, ignoreCase = true)
        val endIndex = startIndex + substring.length
        if (startIndex != -1) {
            spannableString.setSpan(
                BackgroundColorSpan(Color.YELLOW),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textView.text = spannableString
        } else {
            textView.text = fullText
        }
    }

}

private object Callback : DiffUtil.ItemCallback<Joke>() {
    override fun areItemsTheSame(oldItem: Joke, newItem: Joke) = true
    override fun areContentsTheSame(oldItem: Joke, newItem: Joke) = oldItem == newItem
}
