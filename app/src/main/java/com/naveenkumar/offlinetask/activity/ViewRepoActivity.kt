package com.naveenkumar.offlinetask.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.naveenkumar.offlinetask.R
import com.naveenkumar.offlinetask.adapters.BuiltByAdapter
import com.naveenkumar.offlinetask.constants.ID
import com.naveenkumar.offlinetask.data.RepoViewModel
import com.naveenkumar.offlinetask.utils.Utility
import kotlinx.android.synthetic.main.activity_view_repo.*


class ViewRepoActivity : AppCompatActivity() {
    private val repoViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
        ).get(RepoViewModel::class.java)
    }

    val adapter = BuiltByAdapter()
    private var mObjectiveClickedPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_repo)

        iv_back.setOnClickListener {
            onBackPressed()
        }
        initRecyclerView()
        setDataInView()
    }

    private fun initRecyclerView() {
        rv_built_by.layoutManager = GridLayoutManager(this,3)
        rv_built_by.setHasFixedSize(false)
        rv_built_by.adapter = adapter
        rv_built_by.isNestedScrollingEnabled = false

        adapter.setListeners {
            mObjectiveClickedPosition = it

            if (mObjectiveClickedPosition != -1) {
                navigateToViewRepoInGitHub()
            }
        }
    }

    private fun navigateToViewRepoInGitHub() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(adapter.builtBy[mObjectiveClickedPosition].href))
        startActivity(intent)
    }

    private fun setDataInView() {
        intent.extras?.let {
            val id = it.getInt(ID)
            repoViewModel.getRepoById(id).observe(this, Observer { repo ->
                tv_name.text = repo.name
                tv_desc.text = repo.description
                tv_lang.text = repo.language

                val gradientDrawable = (tv_language_color.background as GradientDrawable).mutate()
                val color = repo.languageColor.toString()
                if(color == "null"){
                    tv_language_color.visibility = View.GONE
                }else{
                    val pColor = Color.parseColor(color)
                    (gradientDrawable as GradientDrawable).setColor(pColor)
                }

                tv_star_count.text = repo.stars.toString()
                tv_fork_count.text = repo.forks.toString()
                tv_author.text = repo.author
                btn_github.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.url))
                    startActivity(intent)
                }
                Glide.with(this)
                    .load(repo.avatar)
                    .apply(Utility.options())
                    .into(iv_avatar)
                repo.builtBy?.let { it1 -> adapter.setData(it1) }
            })
        }
    }
}