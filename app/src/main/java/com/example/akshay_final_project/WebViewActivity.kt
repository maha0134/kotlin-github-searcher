package com.example.akshay_final_project

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.akshay_final_project.databinding.ActivityWebViewBinding
/*
* Created by Akshay Mahajan on December 09, 2022
*/
class WebViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWebViewBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url =  intent.getStringExtra(getString(R.string.url_key))

        binding.webViewGitHub.settings.javaScriptEnabled = true
        binding.webViewGitHub.settings.loadWithOverviewMode = true
        binding.webViewGitHub.settings.useWideViewPort = true


        url?.let{
            binding.webViewGitHub.loadUrl(url)
        }
    }
}