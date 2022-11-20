package com.example.dictionary

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.fragment.app.Fragment
import com.example.dictionary.wordsList.WordsList


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.application_main_toolbar))

        val textEdit: EditText = findViewById(R.id.search_word_edit)
        textEdit.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s != null && s.isNotEmpty()) {
                    val args = Bundle()
                    args.putCharSequence("word", s)

                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.dictionary_list_container, WordsList::class.java, args, "WordsList")
                        .addToBackStack(null)
                        .commit()
                }
                else {
                    val fragment: Fragment? = supportFragmentManager.findFragmentByTag("WordsList")
                    if(fragment != null) {
                        supportFragmentManager
                            .beginTransaction()
                            .remove(fragment)
                            .commit()
                    }
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}