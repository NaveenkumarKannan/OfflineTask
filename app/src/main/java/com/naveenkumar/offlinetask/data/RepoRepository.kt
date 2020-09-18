package com.naveenkumar.offlinetask.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.naveenkumar.offlinetask.adapters.ReposAdapter
import com.naveenkumar.offlinetask.room.Repo
import com.naveenkumar.offlinetask.room.RepoDAO
import com.naveenkumar.offlinetask.room.RepoDatabase
import kotlinx.android.synthetic.main.activity_main.*

class RepoRepository(application: Application) {

     private var repoDao: RepoDAO
     private var allRepos: LiveData<List<Repo>>

     init {
         val database =
             RepoDatabase.getInstance(
                 application
             )
         repoDao = database.repoDao()
         allRepos = repoDao.getAllRepos()
     }

     fun insert(repo: Repo) {
         val insertRepoAsyncTask = InsertRepoAsyncTask(
             repoDao
         ).execute(repo)
     }

     fun update(repo: Repo) {
         val updateRepoAsyncTask = UpdateRepoAsyncTask(
             repoDao
         ).execute(repo)
     }

     fun delete(repo: Repo) {
         val deleteRepoAsyncTask = DeleteRepoAsyncTask(
             repoDao
         ).execute(repo)
     }

     fun deleteAll() {
         val deleteAllReposAsyncTask = DeleteAllReposAsyncTask(
             repoDao
         ).execute()
     }

     fun getAllRepos(): LiveData<List<Repo>> {
         return allRepos
     }
    fun searchNamesFromDb(searchText: String): LiveData<List<Repo>> {
        return repoDao.getRepoName(searchText)
    }
    fun getRepoById(id: Int): LiveData<Repo> {
        return repoDao.getRepoById(id)
    }
     companion object {
         private class InsertRepoAsyncTask(val repoDao: RepoDAO) : AsyncTask<Repo, Unit, Unit>() {

             override fun doInBackground(vararg p0: Repo?) {
                 repoDao.insert(p0[0]!!)
             }
         }

         private class UpdateRepoAsyncTask(val repoDao: RepoDAO) : AsyncTask<Repo, Unit, Unit>() {

             override fun doInBackground(vararg p0: Repo?) {
                 repoDao.update(p0[0]!!)
             }
         }

         private class DeleteRepoAsyncTask(val repoDao: RepoDAO) : AsyncTask<Repo, Unit, Unit>() {

             override fun doInBackground(vararg p0: Repo?) {
                 repoDao.delete(p0[0]!!)
             }
         }

         private class DeleteAllReposAsyncTask(val repoDao: RepoDAO) : AsyncTask<Unit, Unit, Unit>() {

             override fun doInBackground(vararg p0: Unit?) {
                 repoDao.deleteAllRepos()
             }
         }
     }
 }