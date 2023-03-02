package com.example.akshay_final_project
/*
* Created by Akshay Mahajan on December 09, 2022
*/
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.akshay_final_project.databinding.ActivityResultsBinding

class ResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data:ArrayList<Users>? = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableArrayListExtra(getString(R.string.user_data_key), Users::class.java)
        }else{
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra(getString(R.string.user_data_key))
        }

        supportActionBar?.title = "${data?.size} ${getString(R.string.menu_results)}"

        binding.recyclerViewMain.layoutManager = LinearLayoutManager(this)

        binding.recyclerViewMain.adapter = data?.let { CustomViewHolderClass.MainAdapter(it) }
    }
}