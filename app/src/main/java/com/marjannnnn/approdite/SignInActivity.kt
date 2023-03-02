package com.marjannnnn.approdite

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.marjannnnn.approdite.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val yellowColor = ContextCompat.getColor(this, R.color.yellow)

        binding.toSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            var email = binding.emailEt.text.toString().trim()
            val password = binding.passET.text.toString().trim()
            var valid = true

            binding.emailLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))
            binding.passwordLayout.setHelperTextColor(ColorStateList.valueOf(Color.RED))

            try {
                if (email.isEmpty()) {
                    binding.emailLayout.boxStrokeColor = Color.RED
                    binding.emailLayout.hintTextColor = ColorStateList.valueOf(Color.RED)
                    binding.emailLayout.helperText = "Email is required"
                    valid = false
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailLayout.boxStrokeColor = Color.RED
                    binding.emailLayout.hintTextColor = ColorStateList.valueOf(Color.RED)
                    binding.emailLayout.helperText = "Please enter a valid email address"
                    valid = false
                } else {
                    binding.emailLayout.helperText = ""
                }

                if (password.isEmpty()) {
                    binding.passwordLayout.boxStrokeColor = Color.RED
                    binding.passwordLayout.hintTextColor = ColorStateList.valueOf(Color.RED)
                    binding.passwordLayout.helperText = "Password is required"
                    valid = false
                } else {
                    binding.passwordLayout.helperText = ""
                }

                if (valid) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                val errorMessage = when (task.exception) {
                                    is FirebaseAuthInvalidUserException -> "Invalid email address"
                                    is FirebaseAuthInvalidCredentialsException -> "Invalid password"
                                    is FirebaseNetworkException -> "Network error"
                                    else -> task.exception
                                }
                                Toast.makeText(
                                    this, "$errorMessage", Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        binding.emailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                binding.emailLayout.boxStrokeColor = yellowColor
                binding.emailLayout.hintTextColor = ColorStateList.valueOf(yellowColor)
                binding.emailLayout.helperText = ""
            }
        })

        binding.passET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // do nothing
            }

            override fun afterTextChanged(s: Editable?) {
                val password = binding.passET.text.toString().trim()

                binding.passwordLayout.boxStrokeColor = yellowColor
                binding.passwordLayout.hintTextColor = ColorStateList.valueOf(yellowColor)
                binding.passwordLayout.helperText = ""
            }

        })
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
