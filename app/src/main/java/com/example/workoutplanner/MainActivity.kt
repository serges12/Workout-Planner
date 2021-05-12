package com.example.workoutplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.workoutplanner.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val currentWorkout = CurrentWorkout()
        val workouts = Workouts()
        val history = History()
        val settings = Settings()
        val exercises = Muscles()
        makeCurrentFragment(currentWorkout)

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.currentWorkout -> makeCurrentFragment(currentWorkout)
                R.id.workouts -> makeCurrentFragment(workouts)
                R.id.history -> makeCurrentFragment(history)
                R.id.settings -> makeCurrentFragment(settings)
                R.id.muscles -> makeCurrentFragment(exercises)
            }
            true
        }

    }


    internal fun makeCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentLayout, fragment)
            commit()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentLayout)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // To close menu with back button
//    override fun onBackPressed() {
//        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
//            drawerLayout.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }
//    }


}