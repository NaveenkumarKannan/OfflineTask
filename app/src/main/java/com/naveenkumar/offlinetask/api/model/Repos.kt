package com.naveenkumar.offlinetask.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Repos {
    @SerializedName("author")
    @Expose
    var author: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("language")
    @Expose
    var language: String? = null

    @SerializedName("languageColor")
    @Expose
    var languageColor: String? = null

    @SerializedName("stars")
    @Expose
    var stars: Int? = null

    @SerializedName("forks")
    @Expose
    var forks: Int? = null

    @SerializedName("currentPeriodStars")
    @Expose
    var currentPeriodStars: Int? = null

    @SerializedName("builtBy")
    @Expose
    var builtBy: List<BuiltBy>? = null

}