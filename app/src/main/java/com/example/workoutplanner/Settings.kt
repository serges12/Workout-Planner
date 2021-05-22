package com.example.workoutplanner

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.workoutplanner.authentication.LoginRegisterActivity
import com.example.workoutplanner.databinding.FragmentSettingsBinding
import com.example.workoutplanner.models.SuggestionModel
import com.example.workoutplanner.models.WorkoutModel
import com.example.workoutplanner.notification.NotificationsSaveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Settings : Fragment() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        val saveData = NotificationsSaveData((activity as MainActivity).applicationContext)
        binding.notificationTimePicker.text = String.format("%02d",saveData.getHour()) + ":" + String.format("%02d",saveData.getMinute())
        binding.notificationTimePicker.setOnClickListener{
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                binding.notificationTimePicker.text = String.format("%02d",hour) + ":" + String.format("%02d",minute)
                (activity as MainActivity).SetTime(hour, minute)
            }
            val timePickerDialog = TimePickerDialog(context,timeSetListener,saveData.getHour(),saveData.getMinute(),true)
            timePickerDialog.show()
        }

        binding.logOutButton.setOnClickListener(){
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder
                .setTitle("Log Out?")
                .setMessage("You are about to be logged out. Are you sure you want to proceed?")
                .setNegativeButton("No"){
                    dialogInterface, _ ->
                        dialogInterface.cancel()
                }
                .setPositiveButton("Yes"){
                    _, _ ->
                    //Handle Logout here
                    auth.signOut()
                    
                    val intent = Intent(activity, LoginRegisterActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            val alert = dialogBuilder.create()
            alert.show()
        }

        binding.resetPasswordButton.setOnClickListener{
            auth.sendPasswordResetEmail(auth.currentUser.email)
                .addOnSuccessListener {
                    Toast.makeText(context, "An reset link has been sent to your email address", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
                }
        }

        binding.deleteAccountButton.setOnClickListener{

            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder
                .setTitle("Delete Account?")
                .setMessage("Deleting an account cannot be undone. Are you sure you want to proceed?")
                .setNegativeButton("No"){
                        dialogInterface, _ ->
                    dialogInterface.cancel()
                }
                .setPositiveButton("Yes"){
                        _, _ ->

                    db.collection("workouts").whereEqualTo("userID", auth.uid).get()
                        .addOnSuccessListener {
                            var batch = db.batch()
                            for (document in it) {
                                batch.delete(document.reference)
                            }
                            batch.commit()
                                .addOnSuccessListener {
                                    db.collection("users").document(auth.uid!!).delete()
                                        .addOnSuccessListener {
                                            auth.currentUser.delete()
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "Account Deleted", Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(activity, LoginRegisterActivity::class.java)
                                                    startActivity(intent)
                                                    activity?.finish()
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                        .addOnFailureListener{
                                            Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
                                        }
                                }
                                .addOnFailureListener{
                                    Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error: " + it.message, Toast.LENGTH_SHORT)
                                .show()
                        }


                }
            val alert = dialogBuilder.create()
            alert.show()

        }

        binding.sendSuggestionButton.setOnClickListener{
            val builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            builder.setTitle("Send us a Suggestion!")
            //Set warning message
            builder.setMessage("You may send us suggestions for things to improve or add! (For example an exercise to add, etc...)")
            // Set up the input
            val input = EditText(requireContext())
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.hint = "Enter suggestion here!"
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            // Set up the buttons
            builder.setPositiveButton("Send", DialogInterface.OnClickListener { _, _ ->
                // Here you get get input text from the Edittext
                var description = input.text.toString()

                val suggestion: SuggestionModel = SuggestionModel(auth.uid!!, auth.currentUser.email,description)

                db.collection("suggestions").add(suggestion)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(context, "Error: "+it.message, Toast.LENGTH_SHORT).show()
                    }
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel() })

            builder.show()
        }

        return binding.root
    }

}