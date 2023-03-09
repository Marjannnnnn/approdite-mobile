package com.marjannnnn.approdite

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.marjannnnn.approdite.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var blueColor: Int = 0
    private lateinit var titleEmailText: TextView
    private lateinit var titlePasswordText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        val star = "*"

        titleEmailText = findViewById(R.id.email_title)
        titlePasswordText = findViewById(R.id.password_title)
        val titleEmail = "Email "
        val titlePassword = "Password "

        val spannableStringEmail = SpannableString(titleEmail + star)
        val spannableStringPassword = SpannableString(titlePassword + star)

        spannableStringEmail.setSpan(
            ForegroundColorSpan(getColor(R.color.red)),
            titleEmail.length,
            spannableStringEmail.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableStringPassword.setSpan(
            ForegroundColorSpan(getColor(R.color.red)),
            titlePassword.length,
            spannableStringPassword.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        titleEmailText.text = spannableStringEmail
        titlePasswordText.text = spannableStringPassword


        binding.toSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passET.text.toString().trim()
            var isValid = true

            validateEmail(email, binding.emailLayout)
            validatePassword(password, binding.passwordLayout)

            if (binding.emailLayout.error != null || binding.passwordLayout.error != null) {
                isValid = false
            }

            if (isValid) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            val errorMessage = when (task.exception) {
                                is FirebaseAuthInvalidUserException -> "Invalid email address"
                                is FirebaseAuthInvalidCredentialsException -> "Invalid password"
                                is FirebaseNetworkException -> "Network error"
                                else -> task.exception?.message
                            }
                            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        binding.emailEt.addTextChangedListener(
            createTextWatcher(binding.emailLayout, ::clearEmail)
        )
        binding.passET.addTextChangedListener(
            createTextWatcher(binding.passwordLayout, ::clearPassword)
        )
    }

    private fun String.isValidEmail(): Boolean {
        return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun validateEmail(email: String, emailLayout: TextInputLayout) {
        if (email.isEmpty()) {
            setError(emailLayout, "Email is required")
        } else if (!email.isValidEmail()) {
            setError(emailLayout, "Please enter a valid email address")
        } else {
            clearError(emailLayout)
        }
    }

    private fun clearEmail(email: String, emailLayout: TextInputLayout) {
        if (email.isNotEmpty()) {
            clearError(emailLayout)
        }
    }

    private fun validatePassword(password: String, passwordLayout: TextInputLayout) {
        if (password.isEmpty()) {
            setError(passwordLayout, "Password is required")
        } else {
            clearError(passwordLayout)
        }
    }

    private fun clearPassword(password: String, passwordLayout: TextInputLayout) {
        if (password.isNotEmpty()) {
            clearError(passwordLayout)
        }
    }

    private fun setError(textInputLayout: TextInputLayout, errorMessage: String) {
        textInputLayout.error = errorMessage
        textInputLayout.boxStrokeColor = Color.RED
        textInputLayout.hintTextColor = ColorStateList.valueOf(Color.RED)
    }

    private fun clearError(textInputLayout: TextInputLayout) {
        blueColor = ContextCompat.getColor(this, R.color.blue2)
        textInputLayout.error = null
        textInputLayout.boxStrokeColor = blueColor
        textInputLayout.hintTextColor = ColorStateList.valueOf(blueColor)
    }

    private fun createTextWatcher(
        textInputLayout: TextInputLayout, validationFunction: (String, TextInputLayout) -> Unit
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    validationFunction(s.toString(), textInputLayout)
                } else {
                    clearError(textInputLayout)
                }
            }
        }
    }
}
