package com.naveenkumar.offlinetask.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.naveenkumar.offlinetask.network.TLSSocketFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

class Utility {

    companion object {
        private var okHttpClient: OkHttpClient? = null

        private var utils: Utility? = null
        lateinit var retrofit: Retrofit
        var baseURL = "https://ghapi.huchen.dev/"
        var toast: Toast? = null

        fun checkInternetConnection(context: Context): Boolean {
            val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // ARE WE CONNECTED TO THE NET
            return (conMgr.activeNetworkInfo != null
                    && conMgr.activeNetworkInfo!!.isAvailable
                    && conMgr.activeNetworkInfo!!.isConnected)
        }

        fun startRetrofit() {
            try {
                okHttpClient = OkHttpClient.Builder()
                    .readTimeout(10000, TimeUnit.SECONDS)
                    .connectTimeout(10000, TimeUnit.SECONDS)
                    .sslSocketFactory(TLSSocketFactory())
                    .build()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            val gson = GsonBuilder()
                .setLenient()
                .create()
            retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
        }

        fun makeText(context: Context, message: String?, value: Int) {
            if (toast != null)
                toast!!.cancel()

            toast = Toast.makeText(context, message, value)
            toast!!.show()
        }
    }

}
