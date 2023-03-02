package com.example.akshay_final_project

/*
* Created by Akshay Mahajan on December 09, 2022
*/
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class InternetConnection( private val context: Context) {

    val isConnected: Boolean
        get() = checkNetworkConnectivity()

    private fun checkNetworkConnectivity(): Boolean {

        // "as" keyword is a smart cast
        val connectivityManager =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val connection = connectivityManager.getNetworkCapabilities(network)

        return connection?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false ||
                connection?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
    }
}