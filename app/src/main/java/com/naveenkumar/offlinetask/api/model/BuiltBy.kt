package com.naveenkumar.offlinetask.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BuiltBy {
    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("href")
    @Expose
    var href: String? = null

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

}