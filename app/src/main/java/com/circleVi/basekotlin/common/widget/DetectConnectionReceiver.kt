package com.circleVi.basekotlin.common.widget


import android.arch.lifecycle.LifecycleObserver
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager

/**
 * Example:
 *
val detectConnectionReceiver = DetectConnectionReceiver(context)
detectConnectionReceiver.setOnConnectionChanged { isConnected ->
    updateConnectionState(isConnected)
}

override fun onResume() {
    super.onResume()
    detectConnectionReceiver.register()
}

override fun onPause() {
    super.onPause()
    detectConnectionReceiver.unregister()
}
 */
class DetectConnectionReceiver(private val context: Context) : BroadcastReceiver(), LifecycleObserver {

    private var isRegisteredReceiver = false
    private var onConnectionChanged: ((isConnected: Boolean) -> Unit)? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.extras != null) {
            val networkInfo = intent.extras.get(WifiManager.EXTRA_NETWORK_INFO) as NetworkInfo
            onConnectionChanged?.invoke(networkInfo.isConnectedOrConnecting)
        }
    }

    fun setOnConnectionChanged(onConnectionChanged: ((isConnected: Boolean) -> Unit)? = null) {
        this.onConnectionChanged = onConnectionChanged
    }

    fun register() {
        if (!isRegisteredReceiver) {
            val intentFilter = IntentFilter()
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(this, intentFilter)
            isRegisteredReceiver = true
        }
    }

    fun unregister() {
        if (isRegisteredReceiver) {
            context.unregisterReceiver(this)
            isRegisteredReceiver = false
        }
    }
}