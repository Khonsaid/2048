package uz.gita.latizx2048.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalStorage private constructor() {
    private val gson = Gson()

    companion object {
        private lateinit var instance: LocalStorage
        private lateinit var pref: SharedPreferences

        fun init(pref: SharedPreferences) {
            if (!::instance.isInitialized) instance = LocalStorage()
            this.pref = pref
        }

        fun getInstance(): LocalStorage = instance
    }

    fun saveScore(score: Int) {
        pref.edit().putInt("score", score).apply()
    }

    fun getScore(): Int = pref.getInt("score", 0)

    fun saveRecord(record: Int) {
        pref.edit().putInt("record", record).apply()
    }

    fun getRecord(): Int = pref.getInt("record", 0)

    fun saveStateGame(list: List<Int>) {
        if (list.isEmpty()) return
        val json = gson.toJson(list)

        if (getSize() == 3) pref.edit().putString("state_game_3", json).apply()
        else if (getSize() == 5) pref.edit().putString("state_game_5", json).apply()
        else if (getSize() == 4) pref.edit().putString("state_game_4", json).apply()
    }

    fun removeStateGame() {
        if (getSize() == 3) pref.edit().remove("state_game_3").apply()
        else if (getSize() == 5) pref.edit().remove("state_game_5").apply()
        else if (getSize() == 4) pref.edit().remove("state_game_4").apply()
    }

    fun getStateGame(): List<Int> {
        var json: String? = null

        if (getSize() == 3) json = pref.getString("state_game_3", null)
        else if (getSize() == 4) json = pref.getString("state_game_4", null)
        else if (getSize() == 5) json = pref.getString("state_game_5", null)

        val type = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    fun setSize(size: Int) {
        pref.edit().putInt("size", size).apply()
    }

    fun getSize(): Int = pref.getInt("size", 4)

    fun getStateGame3(): List<Int> {
        val json = pref.getString("state_game_3", null)

        val type = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, type) ?: emptyMatrix(3)
    }

    fun getStateGame4(): List<Int> {
        val json = pref.getString("state_game_4", null)

        val type = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, type) ?: emptyMatrix(4)
    }

    fun getStateGame5(): List<Int> {
        val json = pref.getString("state_game_5", null)

        val type = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, type) ?: emptyMatrix(5)
    }

    private fun emptyMatrix(size: Int): List<Int> = when (size) {
        3 -> arrayListOf(0, 0, 0, 0, 2, 0, 0, 0, 2)
        5 -> arrayListOf(0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0)
        else -> arrayListOf(0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0)
    }
}