package com.example.dictionary.wordsList

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.dictionary.R
import com.example.dictionary.WordDetailsActivity
import com.example.dictionary.model.WordInfo
import java.util.*
import kotlin.math.min


class WordsListAdapter : Adapter<WordsListAdapter.WordsListViewHolder>() {
    private val wordsInfo: MutableList<WordInfo> = ArrayList()

    class WordsListViewHolder constructor(itemView: View) : ViewHolder(itemView){
        val wordTextView: TextView = itemView.findViewById(R.id.words_list_item_word)
        val firstMeaningTextView: TextView = itemView.findViewById(R.id.words_list_item_meaning_1)
        val secondMeaningTextView: TextView = itemView.findViewById(R.id.words_list_item_meaning_2)
        val thirdMeaningTextView: TextView = itemView.findViewById(R.id.words_list_item_meaning_3)
    }

    fun addWordInfo(wordInfo: WordInfo) {
        wordsInfo.add(wordInfo)
        notifyItemInserted(wordsInfo.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.words_list_item, parent, false)
        return WordsListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordsListViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, WordDetailsActivity::class.java)
            intent.putExtra("wordInfo", wordsInfo[position])
            it.context.startActivity(intent)
        }

        holder.wordTextView.text = wordsInfo[position].word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }
        val definitions = wordsInfo[position].meanings.flatMap { it.definitions }.let { it.subList(0, min(3, it.size)) }
        holder.firstMeaningTextView.text =
            "${if(definitions.size > 1) "1. " else ""}${if(definitions.isNotEmpty()) definitions[0].definition else ""}"
        holder.firstMeaningTextView.visibility = if(definitions.isNotEmpty()) VISIBLE else GONE
        holder.secondMeaningTextView.text =
            "${if(definitions.size > 1) "2. " else ""}${if(definitions.size > 1) definitions[1].definition else ""}"
        holder.secondMeaningTextView.visibility = if(definitions.size > 1) VISIBLE else GONE
        holder.thirdMeaningTextView.text =
            "${if(definitions.size > 1) "3. " else ""}${if(definitions.size > 2) definitions[2].definition else ""}"
        holder.thirdMeaningTextView.visibility = if(definitions.size > 2) VISIBLE else GONE
    }

    override fun getItemCount(): Int {
        return this.wordsInfo.size
    }
}
