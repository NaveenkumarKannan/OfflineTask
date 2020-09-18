package com.naveenkumar.offlinetask.api

import com.naveenkumar.offlinetask.api.model.Repos
import retrofit2.Call
import retrofit2.http.GET


interface ApiManager{
    @GET("repositories")
    fun repoApi():Call<List<Repos?>?>?

    //@GET("/users.json")
    //fun contacts(cb: Callback<List<User?>?>?)
}