package com.example.akshay_final_project
/*
* Created by Akshay Mahajan on December 09, 2022
*/
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Toast
import com.example.akshay_final_project.databinding.ActivityDetailsBinding
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import okhttp3.*
import java.io.IOException

class DetailsActivity : AppCompatActivity() {

    //region properties
    private lateinit var binding: ActivityDetailsBinding
    //endregion

    //region methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = intent.getStringExtra(getString(R.string.details_title_key))

        val url = intent.getStringExtra(getString(R.string.details_url_key))
        val htmlUrl = intent.getStringExtra(getString(R.string.details_html_url_key))

        val content = SpannableString(htmlUrl)
        content.setSpan(UnderlineSpan(), 0 , htmlUrl?.length ?: 0, 0 )
        binding.htmlURLTextView.text = content

        binding.htmlURLTextView.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra(getString(R.string.url_key), htmlUrl)
            startActivity(intent)
        }

        url?.let {
            fetchJson(it)
        }
    }
    //region fetch and display data method

    private fun fetchJson(url: String) {
        // We are using okhttp client here, not Retrofit2
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback { // can't execute from main thread!
            override fun onFailure(call: Call, e: IOException) {
                toast("Request Failed!")
            }

            override fun onResponse(call: Call, response: Response) {

                val body = response.body()?.string()

                val gson = GsonBuilder().create()
                val result = gson.fromJson(body, UserDetails::class.java)

                //display these immediately using the main thread
                runOnUiThread {
                    Picasso.get().load(result.avatar_url).into(binding.avatarImageView)
                    //display data on screen
                    binding.nameTextView.text =  getString(R.string.user_name, result?.name ?: "unknown")
                    binding.locationTextView.text =  getString(R.string.user_location, result?.location ?: "unknown")
                    binding.companyTextView.text =  getString(R.string.user_company, result?.company ?: "unknown")
                    binding.followersTextView.text =  getString(R.string.user_followers, result?.followers?.toString() ?: "unknown")
                    binding.publicGistsTextView.text =  getString(R.string.user_public_gists, result?.public_gists?.toString() ?: "unknown")
                    binding.publicReposTextView.text =  getString(R.string.user_public_repos, result?.public_repos?.toString() ?: "unknown")
                    binding.lastUpdateTextView.text =  getString(R.string.user_last_update, result?.updated_at?.split("T")?.get(0) ?: "unknown")
                    binding.accountCreatedTextView.text =  getString(R.string.user_account_created, result?.created_at?.split("T")?.get(0) ?: "unknown")
                }
            }
        })
    }
    //endregion

    // method to show toast message
    fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    //endregion
}