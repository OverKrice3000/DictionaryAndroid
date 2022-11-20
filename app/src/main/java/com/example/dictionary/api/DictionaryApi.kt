package com.example.dictionary.api

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.example.dictionary.model.WordInfo
import com.example.dictionary.util.callback.OnErrorCallback
import com.example.dictionary.util.callback.OnResultCallback
import com.example.dictionary.util.parser.WordsInfoJsonArrayParser

class DictionaryApi {
    companion object {
        val TAG = "api";
        fun getWordInfo(word: String, context: Context, onResult: OnResultCallback<List<WordInfo>>?, onError: OnErrorCallback?) {
            val url = "https://api.dictionaryapi.dev/api/v2/entries/en/${word}"
            val request = JsonArrayRequest(
                Request.Method.GET, url, null,
                { response ->
                    Log.d(TAG, response.toString())
                    val wordInfo = WordsInfoJsonArrayParser.parse(response)
                    onResult?.onResult(wordInfo)
                },
                { error ->
                    error.message?.let { Log.e(TAG, it) }
                    onError?.onError(error)
                }
            )
            RequestQueueSingleton.getInstance(context).addToRequestQueue(request)
        }
    }
}