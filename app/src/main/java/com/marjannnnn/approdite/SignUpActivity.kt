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
import com.google.firebase.auth.FirebaseAuth
import com.marjannnnn.approdite.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var blueColor: Int = 0
    private lateinit var titleEmailText: TextView
    private lateinit var titlePasswordText: TextView
    private lateinit var titleConfirmPasswordText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        val star = "*"

        titleEmailText = findViewById(R.id.email_title)
        titlePasswordText = findViewById(R.id.password_title)
        titleConfirmPasswordText = findViewById(R.id.confirm_password_title)
        val titleEmail = "Email "
        val titlePassword = "Password "
        val titleConfirmPassword = "Confirm Password "

        val spannableStringEmail = SpannableString(titleEmail + star)
        val spannableStringPassword = SpannableString(titlePassword + star)
        val spannableStringConfirmPassword = SpannableString(titleConfirmPassword + star)

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
        spannableStringConfirmPassword.setSpan(
            ForegroundColorSpan(getColor(R.color.red)),
            titleConfirmPassword.length,
            spannableStringConfirmPassword.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        titleEmailText.text = spannableStringEmail
        titlePasswordText.text = spannableStringPassword
        titleConfirmPasswordText.text = spannableStringConfirmPassword

        binding.toSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passET.text.toString().trim()
            val confirmPass = binding.confirmPassEt.text.toString().trim()
            var isValid = true

            validateEmail(email, binding.emailLayout)
            validatePassword(pass, binding.passwordLayout)
            validateConfirmPassword(confirmPass, binding.confirmPasswordLayout)

            if (binding.emailLayout.error != null || binding.passwordLayout.error != null || binding.confirmPasswordLayout.error != null) {
                isValid = false
            }

            if (isValid) {
                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this, "Email created successfully!", Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this, SignInActivity::class.java))
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


        binding.emailEt.addTextChangedListener(
            createTextWatcher(binding.emailLayout, ::clearEmail)
        )
        binding.passET.addTextChangedListener(
            createTextWatcher(binding.passwordLayout, ::clearPassword)
        )
        binding.confirmPassEt.addTextChangedListener(
            createTextWatcher(
                binding.confirmPasswordLayout, ::clearConfirmPassword
            )
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
        } else if (password.length < 6) {
            setError(passwordLayout, "Password must be at least 6 characters long")
        } else {
            clearError(passwordLayout)
        }
    }

    private fun clearPassword(password: String, passwordLayout: TextInputLayout) {
        if (password.isNotEmpty()) {
            clearError(passwordLayout)
        }
    }

    private fun validateConfirmPassword(
        confirmPassword: String, confirmPasswordLayout: TextInputLayout
    ) {
        val password = binding.passET.text.toString().trim()
        if (confirmPassword.isEmpty()) {
            setError(confirmPasswordLayout, "Confirm Password is required")
        } else if (password.length < 6) {
            setError(confirmPasswordLayout, "Password must be at least 6 characters long")
        } else if (confirmPassword != password) {
            setError(confirmPasswordLayout, "Confirm Password should match Password")
        } else {
            clearError(confirmPasswordLayout)
        }
    }

    private fun clearConfirmPassword(
        confirmPassword: String, confirmPasswordLayout: TextInputLayout
    ) {
        if (confirmPassword.isNotEmpty()) {
            clearError(confirmPasswordLayout)
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
