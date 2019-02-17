package ru.ic218.mychatapp.feature.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build

class NetworkBroadcastListener(private val callback: (Boolean) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var isConnected = false
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                isConnected = connectivityManager.activeNetworkInfo.isConnected
            } else {
                connectivityManager.allNetworkInfo?.forEach {
                    if (it.isConnected) {
                        isConnected = true
                        return@forEach
                    }
                }
            }
            callback.invoke(isConnected)
        }
    }
}