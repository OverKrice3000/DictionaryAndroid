package com.example.dictionary

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dictionary.model.WordInfo
import java.util.*


class WordDetailsActivity : AppCompatActivity() {
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var wordInfo: WordInfo
    private val TAG = "WordDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_details)

        textToSpeech = TextToSpeech(this) {
            if (it != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.ENGLISH
            }
        }

        setSupportActionBar(findViewById(R.id.word_details_toolbar))
        wordInfo = intent.getSerializableExtra("wordInfo") as WordInfo
        supportActionBar?.title = wordInfo.word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val wordTextView = findViewById<TextView>(R.id.word_details_word)
        wordTextView.text = wordInfo.word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }

        val phoneticContainerView = findViewById<LinearLayout>(R.id.word_details_phonetics_container)
        val phoneticsSize = wordInfo.phonetics.size
        var currentPhoneticIndex = 1
        wordInfo.phonetics.forEach { phonetic ->
            if(phonetic.text != null) {
                val phoneticView = TextView(this)
                phoneticView.text = "${if (phoneticsSize > 1) "${currentPhoneticIndex++}. " else ""}${phonetic.text}"
                phoneticContainerView.addView(phoneticView)
            }
        }

        val meaningsContainer = findViewById<LinearLayout>(R.id.word_details_meanings_container)
        wordInfo.meanings.forEach { meaningModel ->
            meaningModel.definitions.forEach { definitionModel ->
                val view = layoutInflater.inflate(R.layout.word_details_meaning, meaningsContainer, false)
                val partOfSpeechView = view.findViewById<TextView>(R.id.word_details_meaning_item_part_of_speech)
                val synonymsView = view.findViewById<TextView>(R.id.word_details_meaning_item_synonyms)
                val antonymsView = view.findViewById<TextView>(R.id.word_details_meaning_item_antonyms)
                val definitionView = view.findViewById<TextView>(R.id.word_details_meaning_item_definition)

                partOfSpeechView.text = meaningModel.partOfSpeech.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                synonymsView.text = "Synonyms: ${definitionModel.synonyms.joinToString()}"
                antonymsView.text = "Antonyms: ${definitionModel.antonyms.joinToString()}"
                definitionView.text = definitionModel.definition

                synonymsView.visibility = if(definitionModel.synonyms.isNotEmpty()) VISIBLE else GONE
                antonymsView.visibility = if(definitionModel.antonyms.isNotEmpty()) VISIBLE else GONE

                meaningsContainer.addView(view)
            }
        }

        val examplesContainerView = findViewById<LinearLayout>(R.id.word_details_examples_container)
        var currentExampleIndex = 1
        val examples = wordInfo.meanings.flatMap { it.definitions }.flatMap { definitionModel ->
            if(definitionModel.example != null) listOf<String>(definitionModel.example) else emptyList()
        }
        val examplesSize = examples.size
        examples.forEach { example ->
            val exampleView = TextView(this)
            exampleView.text = "${if (examplesSize > 1) "${currentExampleIndex++}. " else ""}${example}"
            examplesContainerView.addView(exampleView)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                textToSpeech.shutdown()
                finish()
                return true
            }

            R.id.action_play -> {
                textToSpeech.speak(wordInfo.word, TextToSpeech.QUEUE_ADD, null)
                return true
            }

            R.id.action_settings -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        textToSpeech.shutdown()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.word_details_actions, menu)

        return super.onCreateOptionsMenu(menu)
    }
}