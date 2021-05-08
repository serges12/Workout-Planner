package com.example.workoutplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutplanner.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.fragment_workouts.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        drawerLayout = binding.drawerLayout

//        val navController = this.findNavController(R.id.myNavHostFragment)
//        NavigationUI.setupActionBarWithNavController(this, navController,drawerLayout)
//        NavigationUI.setupWithNavController(binding.navView, navController)
        val currentWorkout = CurrentWorkout()
        val workouts = Workouts()
        val history = History()
        val settings = Settings()

        makeCurrentFragment(currentWorkout)

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.currentWorkout -> makeCurrentFragment(currentWorkout)
                R.id.workouts -> makeCurrentFragment(workouts)
                R.id.history -> makeCurrentFragment(history)
                R.id.settings -> makeCurrentFragment(settings)
            }
            true
        }


    }


    private fun makeCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentLayout, fragment)
            commit()
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.myNavHostFragment)
//        return NavigationUI.navigateUp(navController, drawerLayout)
//    }

    // To close menu with back button
//    override fun onBackPressed() {
//        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
//            drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }
    // wtf
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
////            R.id.workouts -> {
////                Toast.makeText(this,"Not Working Yet", Toast.LENGTH_LONG).show()
//////                Workouts()
////                true
////            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}