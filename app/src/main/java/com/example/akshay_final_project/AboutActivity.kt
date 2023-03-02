package com.example.akshay_final_project
/*
* Created by Akshay Mahajan on December 09, 2022
*/
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.akshay_final_project.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}