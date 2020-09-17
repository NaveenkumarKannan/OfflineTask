package com.naveenkumar.offlinetask.activity

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.mancj.materialsearchbar.MaterialSearchBar
import com.naveenkumar.offlinetask.R
import com.naveenkumar.offlinetask.adapters.ReposAdapter
import com.naveenkumar.offlinetask.api.ApiManager
import com.naveenkumar.offlinetask.api.model.Repos
import com.naveenkumar.offlinetask.data.RepoViewModel
import com.naveenkumar.offlinetask.room.Repo
import com.naveenkumar.offlinetask.utils.Utility
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val repoViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(RepoViewModel::class.java)
    }

    //private lateinit var dialog: ProgressDialog
    private val TAG = "MainActivity"
    val adapter = ReposAdapter()

    private var mObjectiveClickedPosition: Int = -1
    lateinit var searchBar: MaterialSearchBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //dialog = ProgressDialog(this)
        //dialog.setCancelable(false)

        btn_try_again.setOnClickListener {
            setItemsData()
        }
        searchBar = findViewById(R.id.searchBar)
        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {}

            override fun onSearchConfirmed(text: CharSequence?) {}

            override fun onButtonClicked(buttonCode: Int) {
                when (buttonCode) {
                    MaterialSearchBar.BUTTON_BACK -> searchBar.closeSearch()
                }
            }
        })
        searchBar.setCardViewElevation(10)
        searchBar.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                Log.d(
                    "LOG_TAG",
                    javaClass.simpleName + " text changed " + searchBar.text
                )
                searchNames(searchBar.text)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv_repos.layoutManager = LinearLayoutManager(this)
        rv_repos.setHasFixedSize(true)
        rv_repos.adapter = adapter
        repoViewModel.getAllRepos().observe(this, Observer {
            adapter.setData(it)
        })
        adapter.setListeners {
            mObjectiveClickedPosition = it

            if (mObjectiveClickedPosition != -1) {
                navigateToViewRepoActivity()
            }
        }
        srl.setProgressBackgroundColorSchemeColor(
            ContextCompat.getColor(
                this,
                R.color.colorPrimary
            )
        )
        srl.setColorSchemeColors(Color.DKGRAY)

        srl.setOnRefreshListener {
            repoViewModel.deleteAllRepos()
            setItemsData()
        }
        setItemsData()
    }

    override fun onPause() {
        super.onPause()
        /*if (dialog.isShowing) {
            dialog.show()
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        /*if (dialog.isShowing) {
            dialog.show()
        }*/
    }

    private fun setItemsData() {
        searchBar.rootView.findViewById<ImageView>(R.id.mt_search).visibility = View.GONE
        fl_recyclerview.visibility = View.GONE
        ll_no_internet.visibility = View.GONE
        ll_progress_bar.visibility = View.VISIBLE
        if (Utility.checkInternetConnection(this)) {
            //dialog.setMessage("Please wait...")
            //dialog.show()
            srl.isRefreshing = true
            //adapter.repos = mutableListOf()
            Utility.startRetrofit()
            val apiManager = Utility.retrofit.create(ApiManager::class.java)

            val call = apiManager.repoApi()
            call?.enqueue(object : Callback<List<Repos?>?> {
                override fun onResponse(
                    call: Call<List<Repos?>?>?,
                    response: Response<List<Repos?>?>?
                ) {
                    /*if (dialog.isShowing)
                        dialog.dismiss()*/
                    if (response?.body() != null) {
                        Log.e(TAG, Gson().toJson(response.body()))
                        rv_repos.layoutManager = LinearLayoutManager(this@MainActivity)
                        rv_repos.setHasFixedSize(true)
                        rv_repos.adapter = adapter
                        srl.isRefreshing = false
                        //adapter.setData(response?.body() as ArrayList<Repos>)
                        var repos = response.body() as ArrayList<Repos>

                        for (repo in repos) {
                            val newRepo = Repo(
                                author = repo.author as? String,
                                name = repo.name as? String,
                                avatar = repo.avatar as? String,
                                url = repo.url as? String,
                                description = repo.description as? String,
                                language = repo.language as? String,
                                languageColor = repo.languageColor as? String,
                                stars = repo.stars as? Int,
                                forks = repo.forks as? Int,
                                currentPeriodStars = repo.currentPeriodStars as? Int
                            )

                            repoViewModel.insert(newRepo)
                        }
                        searchBar.rootView.findViewById<ImageView>(R.id.mt_search).visibility = View.VISIBLE
                        fl_recyclerview.visibility = View.VISIBLE
                        ll_no_internet.visibility = View.GONE
                        ll_progress_bar.visibility = View.GONE
                    } else {
                        Log.e(TAG, "Null Response")
                    }
                }

                override fun onFailure(call: Call<List<Repos?>?>?, t: Throwable) {
                    srl.isRefreshing = false
                    /*if (dialog.isShowing) {
                        dialog.dismiss()
                    }*/
                    Log.e(TAG, "onFailure $t")
                }
            })
        } else run {
            Utility.makeText(baseContext, "Please Check the Internet", Toast.LENGTH_SHORT)
            srl.isRefreshing = false
            /*if (dialog.isShowing) {
                dialog.dismiss()
            }*/

            searchBar.rootView.findViewById<ImageView>(R.id.mt_search).visibility = View.GONE
            fl_recyclerview.visibility = View.GONE
            ll_no_internet.visibility = View.VISIBLE
            ll_progress_bar.visibility = View.GONE
        }
    }

    private fun searchNames(searchText: String) {
        val searchTextQuery = "%$searchText%"
        repoViewModel.searchNamesFromDb(searchTextQuery)
            .observe(this, Observer { repo: List<Repo> ->
                val adapter = ReposAdapter()
                adapter.setData(repo)
                rv_repos.adapter = adapter
            })
    }

    private fun navigateToViewRepoActivity() {

        /*val intent = Intent(this, ViewRepoActivity::class.java)

        repoViewModel.getAllRepos().observe(this, Observer {
            intent.putExtra(NAME, it[mObjectiveClickedPosition].title)
            intent.putExtra(ADDRESS, it[mObjectiveClickedPosition].address)
            intent.putExtra(PHONE_NUMBER, it[mObjectiveClickedPosition].phoneNumber)
            intent.putExtra(POSITION, it[mObjectiveClickedPosition].position)
        })
        startActivity(intent)*/
    }
}