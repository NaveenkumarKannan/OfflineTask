package com.naveenkumar.offlinetask.activity

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Pair
import android.view.View
import com.naveenkumar.offlinetask.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val logoTransition = "logoTransition"
                val textTransition = "textTransition"
                ivLogo.transitionName = logoTransition
                tv_app_name.transitionName = textTransition
                val activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    Pair<View, String>(ivLogo, logoTransition),
                    Pair<View, String>(tv_app_name, textTransition)
                )
                startActivity(intent, activityOptions.toBundle())
            } else {
                //TODO("VERSION.SDK_INT < LOLLIPOP")
                startActivity(intent)
            }
        }, 2000)
    }
}