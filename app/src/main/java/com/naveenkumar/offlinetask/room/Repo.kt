package com.naveenkumar.offlinetask.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repo_table")
data class Repo(
    var author: String?,
    var name: String?,
    var avatar: String?,
    var url: String?,
    var description: String?,
    var language: String?,
    var languageColor: String?,
    var stars: Int?,
    var forks: Int?,
    var currentPeriodStars: Int?//,
    //var builtBy: List<BuiltBy>
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}