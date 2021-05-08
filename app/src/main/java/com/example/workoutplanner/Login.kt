package com.example.workoutplanner

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.workoutplanner.databinding.FragmentHistoryBinding
import com.example.workoutplanner.databinding.FragmentLoginBinding
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.goToRegister.setOnClickListener{view :View ->
            Navigation.findNavController(view).navigate(R.id.action_login_to_register)
        }
        binding.LoginButton.setOnClickListener(){
            //check credentials
            //if all good start mainactivity
            when{
                //trim{it<=' '} is a lambda function that removes whitespaces at the start and end
                // if email box is empty, make toast asking for email
                TextUtils.isEmpty(binding.emailInputText.text.toString().trim{it<=' '})->{
                    Toast.makeText(
                        requireActivity(),
                        "Please Enter Your Email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //if password box empty, make toast asking for password
                TextUtils.isEmpty(binding.passwordInputText.text.toString().trim{it<=' '})->{
                    Toast.makeText(
                        requireActivity(),
                        "Please Enter Your Password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //if both are not empty
                else->{
                    val email: String = binding.emailInputText.text.toString().trim{it <=' '}
                    val password: String = binding.passwordInputText.text.toString().trim{it <=' '}
                    //create an instance of firebase and register a user with email and password
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener{ task ->
                            //successfully added account to our database
                            if(task.isSuccessful){
                                Toast.makeText(
                                    requireActivity(),
                                    "You were logged in successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //if successfull, take us to main activity with user logged in
                                val intent = Intent(activity,MainActivity::class.java)
                                startActivity(intent)
                                activity?.finish()
                            }
                            //if there was an error
                            else{
                                Toast.makeText(
                                    requireActivity(),
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }


                }
            }

        }

        return binding.root
    }

}