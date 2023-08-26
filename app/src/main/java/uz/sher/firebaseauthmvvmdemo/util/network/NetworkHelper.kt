package uz.sher.firebaseauthmvvmdemo.util.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import javax.inject.Inject

class NetworkHelper @Inject constructor(private val context: Context) {
    @SuppressLint("ObsoleteSdkInt")
    fun isNetworkConnected():Boolean{
        var result=false
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val capabilities=connectivityManager.activeNetwork ?:return false
            val activiveNetwork=connectivityManager.getNetworkCapabilities(capabilities) ?: return false

            result= when{
                activiveNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                activiveNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                activiveNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                else-> false
            }

        }
        else{
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result=when(type){
                        ConnectivityManager.TYPE_WIFI->true
                        ConnectivityManager.TYPE_MOBILE->true
                        ConnectivityManager.TYPE_ETHERNET->true
                        else->false
                    }
                }
            }
        }
        return result
    }
}