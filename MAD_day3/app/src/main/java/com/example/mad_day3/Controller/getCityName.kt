package com.example.mad_day3.Controller

import android.content.Context
import android.content.SharedPreferences

class getCityName {
    fun getCityName(): String? {
        val PREF_NAME = "prefs"
        val KEY_NAME = "city"
        lateinit var sharedPref : SharedPreferences
        var savedName = sharedPref.getString(KEY_NAME, null)
        return savedName
    }
}