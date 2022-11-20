package com.example.dictionary.model

data class WordInfo(
    val word: String,
    val phonetic: String?,
    val phonetics: List<PhoneticModel>,
    val origin: String?,
    val meanings: List<MeaningModel>
): java.io.Serializable