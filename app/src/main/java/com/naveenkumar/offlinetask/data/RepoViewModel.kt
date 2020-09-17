package com.naveenkumar.offlinetask.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.naveenkumar.offlinetask.room.Repo

class RepoViewModel(application: Application) :AndroidViewModel(application) {

     private var repository: RepoRepository =
         RepoRepository(application)
     private var allRepos = repository.getAllRepos()

    fun insert(repo: Repo) {
        repository.insert(repo)
    }

    fun update(repo: Repo) {
        repository.update(repo)
    }

    fun delete(repo: Repo) {
        repository.delete(repo)
    }

    fun deleteAllRepos() {
        repository.deleteAll()
    }

    fun getAllRepos(): LiveData<List<Repo>> {
        return allRepos
    }
    fun searchNamesFromDb(searchText: String): LiveData<List<Repo>> {
        return repository.searchNamesFromDb(searchText)
    }
}