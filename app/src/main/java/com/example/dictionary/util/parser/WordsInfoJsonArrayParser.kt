package com.example.dictionary.util.parser

import com.example.dictionary.model.DefinitionModel
import com.example.dictionary.model.MeaningModel
import com.example.dictionary.model.PhoneticModel
import com.example.dictionary.model.WordInfo
import org.json.JSONArray

class WordsInfoJsonArrayParser {
    companion object {
        fun parse(jsonArray: JSONArray): List<WordInfo> {
            return (0 until jsonArray.length()).map {
                val json = jsonArray.getJSONObject(it)
                json.optInt("j")
                val word = json.getString("word")
                val phonetic = if (json.has("phonetic")) json.getString("phonetic") else null
                val phonetics = parsePhonetics(json.getJSONArray("phonetics"))
                val origin = if (json.has("origin")) json.getString("origin") else null
                val meanings = parseMeanings(json.getJSONArray("meanings"))
                WordInfo(word, phonetic, phonetics, origin, meanings)
            }
        }

        private fun parsePhonetics(jsonArray: JSONArray): List<PhoneticModel> {
            return (0 until jsonArray.length()).map {
                val json = jsonArray.getJSONObject(it)
                val text = if (json.has("text")) json.getString("text") else null
                val audio = if (json.has("audio")) json.getString("audio") else null
                PhoneticModel(text, audio)
            }
        }

        private fun parseMeanings(jsonArray: JSONArray): List<MeaningModel> {
            return (0 until jsonArray.length()).map {
                val json = jsonArray.getJSONObject(it)
                val partOfSpeech = json.getString("partOfSpeech")
                val definitions = parseDefinitions(json.getJSONArray("definitions"))
                MeaningModel(partOfSpeech, definitions)
            }
        }

        private fun parseDefinitions(jsonArray: JSONArray): List<DefinitionModel> {
            return (0 until jsonArray.length()).map { definitionPosition ->
                val json = jsonArray.getJSONObject(definitionPosition)
                val definition = json.getString("definition")
                val example = if (json.has("example")) json.getString("example") else null
                val synonymsJsonArray = json.getJSONArray("synonyms")
                val antonymsJsonArray = json.getJSONArray("antonyms")
                val synonyms = (0 until synonymsJsonArray.length()).map { synonymPosition ->
                    synonymsJsonArray.getString(synonymPosition)
                }
                val antonyms = (0 until antonymsJsonArray.length()).map { antonymPosition ->
                    antonymsJsonArray.getString(antonymPosition)
                }
                DefinitionModel(definition, example, synonyms, antonyms)
            }
        }
    }
}