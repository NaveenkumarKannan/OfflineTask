package com.naveenkumar.offlinetask.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mancj.materialsearchbar.MaterialSearchBar
import com.naveenkumar.offlinetask.R
import com.naveenkumar.offlinetask.adapters.ReposAdapter
import com.naveenkumar.offlinetask.api.ApiManager
import com.naveenkumar.offlinetask.api.model.Repos
import com.naveenkumar.offlinetask.constants.*
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

    val adapter = ReposAdapter()

    private var mObjectiveClickedPosition: Int = -1
    lateinit var searchBar: MaterialSearchBar
    var isLocalRepo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_try_again.setOnClickListener {
            setItemsData()
        }
        searchBar = findViewById(R.id.searchBar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ivLogo.transitionName = "logoTransition"
            searchBar.rootView.findViewById<TextView>(R.id.mt_placeholder).transitionName = "textTransition"
        }
        searchBar.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {
                if(enabled){
                    ivLogo.visibility = View.GONE
                }else{
                    ivLogo.visibility = View.VISIBLE
                }
            }

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
            if(it.isEmpty()) {
                isLocalRepo = false
            }else {
                isLocalRepo = true

                searchBar.rootView.findViewById<ImageView>(R.id.mt_search).visibility = View.VISIBLE
                fl_recyclerview.visibility = View.VISIBLE
                ll_no_internet.visibility = View.GONE
                ll_progress_bar.visibility = View.GONE
            }
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
            setItemsData()
        }
        setItemsData()
    }


    private fun setItemsData() {
        if(!isLocalRepo){
            searchBar.rootView.findViewById<ImageView>(R.id.mt_search).visibility = View.GONE
            fl_recyclerview.visibility = View.GONE
            ll_no_internet.visibility = View.GONE
            ll_progress_bar.visibility = View.VISIBLE
        }else{
            searchBar.rootView.findViewById<ImageView>(R.id.mt_search).visibility = View.VISIBLE
            fl_recyclerview.visibility = View.VISIBLE
            ll_no_internet.visibility = View.GONE
            ll_progress_bar.visibility = View.GONE
        }


        if (Utility.checkInternetConnection(this)) {
            srl.isRefreshing = true
            Utility.startRetrofit()
            val apiManager = Utility.retrofit.create(ApiManager::class.java)

            val call = apiManager.repoApi()
            call?.enqueue(object : Callback<List<Repos?>?> {
                override fun onResponse(
                    call: Call<List<Repos?>?>?,
                    response: Response<List<Repos?>?>?
                ) {
                    if (response?.body() != null) {
                        rv_repos.layoutManager = LinearLayoutManager(this@MainActivity)
                        rv_repos.setHasFixedSize(true)
                        rv_repos.adapter = adapter
                        srl.isRefreshing = false

                        repoViewModel.deleteAllRepos()
                        val repos = response.body() as ArrayList<Repos>
                        for (repo in repos) {
                            val newRepo = Repo(
                                author = repo.author,
                                name = repo.name,
                                avatar = repo.avatar,
                                url = repo.url,
                                description = repo.description,
                                language = repo.language,
                                languageColor = repo.languageColor,
                                stars = repo.stars,
                                forks = repo.forks,
                                currentPeriodStars = repo.currentPeriodStars,
                                builtBy = repo.builtBy
                            )

                            repoViewModel.insert(newRepo)
                        }
                        searchBar.rootView.findViewById<ImageView>(R.id.mt_search).visibility = View.VISIBLE
                        fl_recyclerview.visibility = View.VISIBLE
                        ll_no_internet.visibility = View.GONE
                        ll_progress_bar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<Repos?>?>?, t: Throwable) {
                    srl.isRefreshing = false
                }
            })
        } else run {
            Utility.makeText(baseContext, "No network connection", Toast.LENGTH_SHORT)
            srl.isRefreshing = false

            if(!isLocalRepo){
                searchBar.rootView.findViewById<ImageView>(R.id.mt_search).visibility = View.GONE
                fl_recyclerview.visibility = View.GONE
                ll_no_internet.visibility = View.VISIBLE
                ll_progress_bar.visibility = View.GONE
            }else{
                searchBar.rootView.findViewById<ImageView>(R.id.mt_search).visibility = View.VISIBLE
                fl_recyclerview.visibility = View.VISIBLE
                ll_no_internet.visibility = View.GONE
                ll_progress_bar.visibility = View.GONE
            }
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
        val intent = Intent(this, ViewRepoActivity::class.java)
        repoViewModel.getAllRepos().observe(this, Observer {
            intent.putExtra(ID, it[mObjectiveClickedPosition].id)
            /*intent.putExtra(AUTHOR, it[mObjectiveClickedPosition].author)
            intent.putExtra(NAME, it[mObjectiveClickedPosition].name)
            intent.putExtra(AVATAR, it[mObjectiveClickedPosition].avatar)
            intent.putExtra(URL, it[mObjectiveClickedPosition].url)
            intent.putExtra(DESCRIPTION, it[mObjectiveClickedPosition].description)
            intent.putExtra(LANGUAGE, it[mObjectiveClickedPosition].language)
            intent.putExtra(LANGUAGE_COLOR, it[mObjectiveClickedPosition].languageColor)
            intent.putExtra(STARS, it[mObjectiveClickedPosition].stars)
            intent.putExtra(FORKS, it[mObjectiveClickedPosition].forks)
            intent.putExtra(CURRENT_PERIOD_STARS, it[mObjectiveClickedPosition].currentPeriodStars)
            intent.putExtra(URL, it[mObjectiveClickedPosition].url)*/
        })
        startActivity(intent)
    }
    override fun onBackPressed() {
        Utility.onBack(this)
    }
}