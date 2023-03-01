package com.marjannnnn.approdite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.marjannnnn.approdite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
//    private lateinit var logOutBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        replaceFragment(HomeFragment())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomBar.onItemSelected = { index ->
            Log.i("woi", "$index")
            when (index) {
                0 -> replaceFragment(HomeFragment())
                1 -> replaceFragment(DashboardFragment())
                2 -> replaceFragment(ProfileFragment())
            }
            true
        }
//        logOutBtn = findViewById(R.id.logOutBtn)

//        logOutBtn.setOnClickListener {
//            firebaseAuth.signOut()
//            val intent = Intent(this, SignInActivity::class.java)
//            startActivity(intent)
//            Toast.makeText(
//                this, "Logout successfully!", Toast.LENGTH_SHORT
//            ).show()
//            finish()
//        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser == null) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}