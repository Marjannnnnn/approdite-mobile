package com.marjannnnn.approdite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.marjannnnn.approdite.databinding.ActivityMainBinding
import com.marjannnnn.approdite.fragment.DashboardFragment
import com.marjannnnn.approdite.fragment.HomeFragment
import com.marjannnnn.approdite.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        val user = firebaseAuth.currentUser
        if (user == null) {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

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
        }

    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        val builder = AlertDialog.Builder(this)
//        builder.setMessage(
//            "Are you sure you want to leave?"
//        ).setCancelable(false).setPositiveButton("Yes") { _, _ ->
//            finishAffinity()
//        }.setNegativeButton("No", null)
//        val alert = builder.create()
//        alert.show()
//    }
}