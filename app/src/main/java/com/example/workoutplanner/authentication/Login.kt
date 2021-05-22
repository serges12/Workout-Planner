package com.example.workoutplanner.authentication

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.workoutplanner.MainActivity
import com.example.workoutplanner.R
import com.example.workoutplanner.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class Login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val RC_SIGN_IN = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        //if user is already logged in, go straight to main activity
        if (FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

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
                                val intent = Intent(activity, MainActivity::class.java)
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

        binding.loginWithGoogleButton.setOnClickListener{
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        return binding.root
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(context, "You were logged in successfully.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Error: "+task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}