package com.example.workoutplanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.workoutplanner.databinding.ActivityMainBinding
import com.example.workoutplanner.notification.NotificationsSaveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.drawer_menu_header.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.setTitle("Home")

        //Set navigation header view
        val headerView:View = binding.navView.inflateHeaderView(R.layout.drawer_menu_header)
        headerView.emailText.text = "Logged in as " + FirebaseAuth.getInstance().currentUser.email.toString()

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

    //Set the alarm from Settingd fragment
    fun SetTime(Hours:Int, Minutes:Int, on:Boolean){
        val saveData = NotificationsSaveData(applicationContext)
        saveData.SaveData(Hours,Minutes,on)
        saveData.setAlarm()
    }

    //To copy something to clipboard
    fun copyToClipboard(text: String){
        val clipboardManager: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData: ClipData = ClipData.newPlainText("copied text", text)
        clipboardManager.setPrimaryClip(clipData)
    }
}