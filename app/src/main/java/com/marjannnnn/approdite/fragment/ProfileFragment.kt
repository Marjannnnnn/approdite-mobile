package com.marjannnnn.approdite.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.google.firebase.auth.FirebaseAuth
import com.marjannnnn.approdite.R


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Are you sure you want to leave?").setCancelable(false)
                    .setPositiveButton("Yes") { _, _ ->
                        requireActivity().finishAffinity()
                    }.setNegativeButton("No", null)
                val alert = builder.create()
                alert.show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        // Get user email
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userEmail = currentUser?.email

        // Set user email to TextView
        val userEmailTextView = view.findViewById<TextView>(R.id.userEmail)
        userEmailTextView.text = userEmail

        // Get user creation time
        val userCreatedAt = currentUser?.metadata?.creationTimestamp

        // Set user creation time to TextView
        val userCreatedAtTextView = view.findViewById<TextView>(R.id.userCreatedAt)
        userCreatedAtTextView.text = userCreatedAt?.let { timestamp ->
            val date = java.util.Date(timestamp)
            java.text.SimpleDateFormat("dd/MM/yyyy").format(date)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}