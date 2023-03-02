package com.example.akshay_final_project
/*
* Created by Akshay Mahajan on December 09, 2022
*/
import android.content.Context
import android.content.SharedPreferences

@Suppress("UNUSED")
class LocalStorage (context: Context = TheApp.context) {

    // region Properties

    private val preferencesName = context.getString(R.string.app_name) // use the app name
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferencesName,
        Context.MODE_PRIVATE)

    // endregion

    // region Methods

    fun contains(KEY_NAME: String): Boolean{
        return sharedPreferences.contains(KEY_NAME)
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun getAll(): Map<String, *>{
        return sharedPreferences.all
    }

    // endregion

    // region Set methods

    fun save(KEY_NAME: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, text)
        editor.apply()
    }

    fun save(KEY_NAME:String, value: Int){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(KEY_NAME,value)
        editor.apply()
    }

    fun save(KEY_NAME:String, value: Long){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putLong(KEY_NAME,value)
        editor.apply()
    }

    fun save(KEY_NAME:String, value: Float){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putFloat(KEY_NAME,value)
        editor.apply()
    }

    fun save(KEY_NAME:String, value: Boolean){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(KEY_NAME,value)
        editor.apply()
    }

    fun save(KEY_NAME:String, value: Set<String>){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putStringSet(KEY_NAME,value)
        editor.apply()
    }

    // endregion

    // region Get methods
    fun getValueString(KEY_NAME: String): String? {
        return sharedPreferences.getString(KEY_NAME, null)
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sharedPreferences.getInt(KEY_NAME,0)
    }

    fun getValueLong(KEY_NAME: String): Long {
        return sharedPreferences.getLong(KEY_NAME,0)
    }

    fun getValueFloat(KEY_NAME: String): Float {
        return sharedPreferences.getFloat(KEY_NAME,0.0f)
    }

    fun getValueBoolean(KEY_NAME: String): Boolean {
        return sharedPreferences.getBoolean(KEY_NAME,false)
    }

    fun getValueSetString(KEY_NAME: String): Set<String>? {
        return sharedPreferences.getStringSet(KEY_NAME,null)
    }

    // endregion
}