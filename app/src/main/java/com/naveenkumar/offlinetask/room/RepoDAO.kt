package com.naveenkumar.offlinetask.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.naveenkumar.offlinetask.room.Repo

@Dao
interface RepoDAO {

    @Insert
    fun insert(repo: Repo)

    @Update
    fun update(repo: Repo)

    @Delete
    fun delete(repo: Repo)

    @Query("DELETE FROM repo_table")
    fun deleteAllRepos()

    @Query("SELECT * FROM repo_table")
    fun getAllRepos(): LiveData<List<Repo>>

    @Query("SELECT * FROM repo_table  WHERE name LIKE :query")
    fun getRepoName(query: String): LiveData<List<Repo>>
}