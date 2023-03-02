package com.example.akshay_final_project

/*
* Created by Akshay Mahajan on December 09, 2022
*/

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.akshay_final_project.databinding.ActivityMainBinding
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    //region Properties

    private lateinit var binding: ActivityMainBinding
    private val minPage = 1
    private val maxPage = 100
    private val startPage = 30
    private val localStorage = LocalStorage()
    private val baseURL = "https://api.github.com/search/"

    //endregion

    //region Methods

    //region Android lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //check for internet connectivity
        val internetConnection = InternetConnection(this)
        if (!internetConnection.isConnected){
            AlertDialog.Builder(this)
                .setTitle(R.string.message_title)
                .setMessage(R.string.message_text)
                .setIcon(R.drawable.ic_baseline_network_check_24)
                .setNegativeButton(R.string.quit){_,_->finish()}.setCancelable(false).show()
        }else {

            binding.perPageNumberPicker.minValue = minPage
            binding.perPageNumberPicker.maxValue = maxPage

            checkLocalStorageAndSetDefaults()

            binding.searchButton.setOnClickListener {
                fetchJSONData()
            }

            //region toggle button enable state
            binding.searchUser.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    binding.noResultsTextView.text = ""
                    binding.searchButton.isEnabled = true
                }

            })
            //endregion

            //region Keyboard-support return key
            binding.searchUser.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    //custom
                    if (binding.searchButton.isEnabled) {
                        binding.searchButton.callOnClick()
                    }

                    return@OnKeyListener true
                }
                false
            })
            //endregion
        }
    }

    override fun onStop() {
        super.onStop()

        val perPageNumber = binding.perPageNumberPicker.value
        val minNumberOfFollowers = binding.minFollowersEditText.text
        val minNumberOfRepos = binding.minReposEditText.text

        //store page number if default value changed by user
        if(perPageNumber != startPage){
            localStorage.save(getString(R.string.page_size_key),perPageNumber)
        }
        //check if there's a value in the text fields and store that value in local storage
        if(!TextUtils.isEmpty(minNumberOfFollowers)){
            localStorage.save(getString(R.string.followers_key),minNumberOfFollowers.toString().toInt())
        }

        if(!TextUtils.isEmpty(minNumberOfRepos)){
            localStorage.save(getString(R.string.repos_key),minNumberOfRepos.toString().toInt())
        }
    }

    //endregion

    private fun checkLocalStorageAndSetDefaults(){
        //per page size
        if(localStorage.contains(getString(R.string.page_size_key))){
            val valueFromLocalStorage = localStorage.getValueInt(getString(R.string.page_size_key))
            binding.perPageNumberPicker.value = valueFromLocalStorage
        }else{
            binding.perPageNumberPicker.value = startPage
        }

        //minimum repos
        if(localStorage.contains(getString(R.string.repos_key))){
            val valueFromLocalStorage = localStorage.getValueInt(getString(R.string.repos_key))
            binding.minReposEditText.setText(valueFromLocalStorage.toString())
        }else{
            binding.minReposEditText.setText("0")
        }

        //min followers
        if(localStorage.contains(getString(R.string.followers_key))){
            val valueFromLocalStorage = localStorage.getValueInt(getString(R.string.followers_key))
            binding.minFollowersEditText.setText(valueFromLocalStorage.toString())
        }else{
            binding.minFollowersEditText.setText("0")
        }
    }

    private fun fetchJSONData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val restApi = retrofit.create(RestApi::class.java)

        if(TextUtils.isEmpty(binding.minReposEditText.text)){
            binding.minReposEditText.setText("0")
        }

        if(TextUtils.isEmpty(binding.minFollowersEditText.text)){
            binding.minFollowersEditText.setText("0")
        }



        val minNumberOfFollowers = binding.minFollowersEditText.text.toString()
        val minNumberOfRepos = binding.minReposEditText.text.toString()

        val searchString = "${binding.searchUser.text} repos:>=$minNumberOfRepos followers:>=$minNumberOfFollowers"
        val call = restApi.getUserData(searchString, binding.perPageNumberPicker.value)

        binding.progressBar.visibility = View.VISIBLE

        call.enqueue(object: Callback<ResponseDataClass> {
            override fun onResponse(
                call: Call<ResponseDataClass>,
                response: Response<ResponseDataClass>
            ) {
                val responseBody = response.body()

                val users = responseBody?.items
                val numberOfUsers = users?.size  ?: 0

                if(numberOfUsers>0){
                    val intent = Intent(TheApp.context,ResultsActivity::class.java)

                    intent.putParcelableArrayListExtra(getString(R.string.user_data_key),users)
                    startActivity(intent)
                }else{
                    binding.noResultsTextView.text = getString(R.string.no_results,binding.searchUser.text)
                    binding.searchButton.isEnabled = false
                }


                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ResponseDataClass>, t: Throwable) {
                toast(t.message.toString())
                binding.progressBar.visibility = View.GONE
            }

        }
        )

    }

    //region Options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this)
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //endregion

    //region Keyboard - Hide the soft keyboard when no input control has focus

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        currentFocus?.let {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken,0)
        }
        return super.dispatchTouchEvent(ev)
    }

    //endregion

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    //endregion
}