package com.richardkuiper.cleanarchitecture.stackoverflowusers.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.richardkuiper.cleanarchitecture.R
import com.richardkuiper.cleanarchitecture.common.InjectorBaseActivity

class TopUsersActivity : InjectorBaseActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_users)

        navController = Navigation.findNavController(this, R.id.activityNavigationFragment).apply {
            setGraph(R.navigation.nav_users)
        }
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            R.id.topUserDetailFragment -> {
                navController.popBackStack()

                (this as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
            else -> super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}