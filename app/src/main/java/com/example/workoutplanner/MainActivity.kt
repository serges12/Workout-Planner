package com.example.workoutplanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.workoutplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Notifications
        val saveData = NotificationsSaveData(applicationContext)

        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        setupWithNavController(binding.navView, navController)

        //we need to setup appBarConfiguration for our hamburgermenu to know which are the top-level destinations
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.currentWorkout, R.id.workouts, R.id.muscles, R.id.customWorkout, R.id.settings),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.currentWorkout, R.id.workouts, R.id.muscles, R.id.customWorkout, R.id.settings),
            drawerLayout
        )
        //As it turns out, the appBarConfiguration needs to be used as the parameter for navigateUp so that the top-level destinations setup are obeyed:
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//     To close menu with back button
    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun SetTime(Hours:Int, Minutes:Int){
        val saveData = NotificationsSaveData(applicationContext)
        saveData.SaveData(Hours,Minutes)
        saveData.setAlarm()
    }
}