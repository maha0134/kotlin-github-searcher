package com.example.akshay_final_project

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/*
* Created by Akshay Mahajan on November 03, 2022
*/
class TheApp:Application(){
    override fun onCreate(){
        super.onCreate()
        context = applicationContext
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
            private set
    }
}