package com.naveenkumar.offlinetask.activity

import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.naveenkumar.offlinetask.R
import com.naveenkumar.offlinetask.adapters.ReposAdapter
import com.naveenkumar.offlinetask.api.ApiManager
import com.naveenkumar.offlinetask.api.model.Repo
import com.naveenkumar.offlinetask.utils.Utility
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var dialog: ProgressDialog
    private val TAG = "MainActivity"
    val adapter = ReposAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialog = ProgressDialog(this)
        dialog.setCancelable(false)

        srl.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.colorPrimary))
        srl.setColorSchemeColors(Color.DKGRAY)

        srl.setOnRefreshListener {
            setItemsData()
        }
        setItemsData()
    }
    private fun setItemsData() {
        if(Utility.checkInternetConnection(this)){
            dialog.setMessage("Please wait...")
            dialog.show()
            srl.isRefreshing = true
            adapter.repo = mutableListOf()
            Utility.startRetrofit()
            val apiManager = Utility.retrofit.create(ApiManager::class.java)

            val call = apiManager.repoApi()
            call?.enqueue(object : Callback<List<Repo?>?> {
                override fun onResponse(
                    call: Call<List<Repo?>?>?,
                    response: Response<List<Repo?>?>?
                ) {
                    if (dialog.isShowing)
                        dialog.dismiss()
                    if (response?.body() != null) {
                        Log.e(TAG, Gson().toJson(response.body()))
                        rv_repos.layoutManager = LinearLayoutManager(this@MainActivity)
                        rv_repos.setHasFixedSize(true)
                        rv_repos.adapter = adapter
                        srl.isRefreshing = false
                        adapter.setData(response.body() as ArrayList<Repo>)
                    }else{
                        Log.e(TAG, "Null Response")
                    }
                }

                override fun onFailure(call: Call<List<Repo?>?>?, t: Throwable) {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                    Log.e(TAG, "onFailure $t")
                }
            })
        } else run {
            Utility.makeText(baseContext, "Please Check the Internet", Toast.LENGTH_SHORT)
            if (dialog.isShowing) {
                dialog.dismiss()
            }
        }
    }
}