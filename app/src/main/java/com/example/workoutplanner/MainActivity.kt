package com.example.workoutplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.workoutplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

//        val currentWorkout = CurrentWorkout()
//        val workouts = Workouts()
//        val history = History()
//        val settings = Settings()
//        val exercises = Muscles()
//        makeCurrentFragment(currentWorkout)
//
//        binding.bottomNav.setOnNavigationItemSelectedListener {
//            when(it.itemId) {
//                R.id.currentWorkout -> makeCurrentFragment(currentWorkout)
//                R.id.workouts -> makeCurrentFragment(workouts)
//                R.id.history -> makeCurrentFragment(history)
//                R.id.settings -> makeCurrentFragment(settings)
//                R.id.muscles -> makeCurrentFragment(exercises)
//            }
//            true
//        }

    }


//    internal fun makeCurrentFragment(fragment: Fragment){
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.fragmentLayout, fragment)
//            commit()
//        }
//    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

//     To close menu with back button
    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle item selection
//        return when (item.itemId) {
//            R.id.currentWorkout -> {
//                CurrentWorkout()
//                true
//            }
//            R.id.history -> {
//                History()
//                true
//            }
//            R.id.settings -> {
//                Settings()
//                true
//            }
//            R.id.workouts -> {
//                Workouts()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}