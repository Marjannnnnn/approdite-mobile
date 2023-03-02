package com.marjannnnn.approdite

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.marjannnnn.approdite.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.toSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passET.text.toString().trim()
            val confirmPass = binding.confirmPassEt.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailEt.error = "Email is required"
                binding.emailEt.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailEt.error = "Please enter a valid email address"
                binding.emailEt.requestFocus()
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                binding.passET.error = "Password is required"
                binding.passET.requestFocus()
                return@setOnClickListener
            }

            if (pass.length < 6) {
                binding.passET.error = "Password should be at least 6 characters"
                binding.passET.requestFocus()
                return@setOnClickListener
            }

            if (confirmPass.isEmpty()) {
                binding.confirmPassEt.error = "Please confirm your password"
                binding.confirmPassEt.requestFocus()
                return@setOnClickListener
            }

            if (pass != confirmPass) {
                binding.confirmPassEt.error = "Password is not matching"
                binding.confirmPassEt.requestFocus()
                return@setOnClickListener
            }

            firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this, "Email created successfully!", Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Failed to create email: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
