package com.example.dictionary.util.callback

import com.android.volley.VolleyError

interface OnErrorCallback {
    fun onError(error: VolleyError)
}