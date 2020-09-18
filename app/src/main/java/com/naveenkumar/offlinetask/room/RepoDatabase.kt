package com.naveenkumar.offlinetask.room

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Repo::class], version = 1)
@TypeConverters(value = [BuiltByConverter::class])
abstract class RepoDatabase: RoomDatabase() {

    abstract fun repoDao(): RepoDAO

    companion object {
        @Volatile private var instance: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase {
            if (instance == null) {
                synchronized(RepoDatabase::class.java) {
                        instance = Room.databaseBuilder(context.applicationContext, RepoDatabase::class.java, "repo_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build()
                }
            }
            return instance as RepoDatabase
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(
                    instance
                )
                    .execute()
            }
        }
    }

    class PopulateDbAsyncTask(db: RepoDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val repoDao = db?.repoDao()

        override fun doInBackground(vararg p0: Unit?) {
        }
    }

}