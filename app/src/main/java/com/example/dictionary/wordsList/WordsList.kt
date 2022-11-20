package com.example.dictionary.wordsList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.example.dictionary.R
import com.example.dictionary.api.DictionaryApi
import com.example.dictionary.model.WordInfo
import com.example.dictionary.util.callback.OnErrorCallback
import com.example.dictionary.util.callback.OnResultCallback

class WordsList : Fragment(R.layout.fragment_words_list) {
    val TAG = "WordsList"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val recyclerView: RecyclerView? = view?.findViewById(R.id.dictionary_list)
        if(recyclerView == null) {
            Log.e(TAG, "ERROR! RECYCLER VIEW NOT FOUND!")
            return view
        }
        val word = this.arguments?.getCharSequence("word")
        if(word == null) {
            Log.e(TAG, "ERROR! FRAGMENT WITHOUT ARGUMENTS CREATED!")
            return view
        }
        val activity = activity
        if(activity == null) {
            Log.e(TAG, "ERROR! ACTIVITY IS NULL!")
            return view
        }
        val wordsListAdapter = WordsListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.adapter = wordsListAdapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        DictionaryApi.getWordInfo(word.toString(), activity.applicationContext,
            object: OnResultCallback<List<WordInfo>> {
                override fun onResult(result: List<WordInfo>) {
                    result.forEach {
                        wordsListAdapter.addWordInfo(it)
                    }
                }
            },
            object: OnErrorCallback {
                override fun onError(error: VolleyError) {

                }
            })
        return view
    }
}