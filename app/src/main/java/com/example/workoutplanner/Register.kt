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
import com.example.workoutplanner.databinding.FragmentLoginBinding
import com.example.workoutplanner.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.*

class Register : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        binding.goToLogin.setOnClickListener{view :View ->
            Navigation.findNavController(view).navigate(R.id.action_register_to_login)
        }

        binding.RegisterButton.setOnClickListener(){
            when{
                //trim{it<=' '} is a lambda function that removes whitespaces at the start and end
                // if email box is empty, make toast asking for email
                TextUtils.isEmpty(binding.emailInputText.text.toString().trim{it<=' '})->{
                    Toast.makeText(
                        requireActivity(),
                        "Please Enter An Email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //if password box empty, make toast asking for password
                TextUtils.isEmpty(binding.passwordInputText.text.toString().trim{it<=' '})->{
                    Toast.makeText(
                        requireActivity(),
                        "Please Enter A password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //if both are not empty
                else->{
                    val email: String = binding.emailInputText.text.toString().trim{it <=' '}
                    val password: String = binding.passwordInputText.text.toString().trim{it <=' '}
                    //create an instance of firebase and register a user with email and password
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(

                            OnCompleteListener<AuthResult> { task->
                                //successfully added account to our database
                                if(task.isSuccessful){
                                    val firebaseUser : FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        requireActivity(),
                                        "You were registered successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    //if successfull, take us back to login so user logs in (we can modify it to login directly from here)
                                    Navigation.findNavController(requireView()).navigate(R.id.action_register_to_login)
                                }
                                //if there was an error
                                else{
                                    Toast.makeText(
                                        requireActivity(),
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )

                }
            }

        }
        return binding.root
    }

}