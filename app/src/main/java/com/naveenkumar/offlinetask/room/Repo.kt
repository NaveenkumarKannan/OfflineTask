package com.naveenkumar.offlinetask.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.naveenkumar.offlinetask.api.model.BuiltBy

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
    var currentPeriodStars: Int?,
    @TypeConverters(BuiltByConverter::class)
    var builtBy: List<BuiltBy>?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}