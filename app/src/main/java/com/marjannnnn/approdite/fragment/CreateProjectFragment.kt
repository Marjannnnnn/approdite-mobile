package com.marjannnnn.approdite.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.android.material.textfield.TextInputEditText
import com.marjannnnn.approdite.MainActivity
import com.marjannnnn.approdite.databinding.FragmentCreateProjectBinding
import com.marjannnnn.approdite.db.DatabaseHandler
import com.marjannnnn.approdite.model.Project
import java.text.SimpleDateFormat
import java.util.*

class CreateProjectFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    // deklarasi binding view binding
    private var _binding: FragmentCreateProjectBinding? = null
    private val binding get() = _binding!!

    // deklarasi date picker dialog dan calendar
    private lateinit var datePicker: DatePickerDialog
    private lateinit var selectedDateEditText: TextInputEditText
    private val calendar = Calendar.getInstance()

    // deklarasi database handler
    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateProjectBinding.inflate(inflater, container, false)
        databaseHandler = DatabaseHandler(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Replace fragment on back press
                (activity as MainActivity).replaceFragment(DashboardFragment())
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.close.setOnClickListener {
            (activity as MainActivity).replaceFragment(DashboardFragment())
        }

        // inisialisasi date picker dialog dengan tanggal saat ini
        datePicker = DatePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // aksi ketika start date edit text di-klik
        binding.startDateEdittext.setOnClickListener {
            selectedDateEditText = binding.startDateEdittext
            datePicker.show()
        }

        // aksi ketika end date edit text di-klik
        binding.endDateEdittext.setOnClickListener {
            selectedDateEditText = binding.endDateEdittext
            datePicker.show()
        }

        // aksi ketika tombol create di-klik
        binding.createButton.setOnClickListener {
            // mengambil data dari input field
            val projectName = binding.projectNameEdittext.text.toString()
            val taskName = binding.taskNameEdittext.text.toString()
            val assignTo = binding.assignToEdittext.text.toString()
            val sprintEditText = binding.sprintEdittext.text.toString()
            val attachment = binding.attachmentEdittext.text.toString()
            val startDateEditText = binding.startDateEdittext.text.toString()
            val endDateEditText = binding.endDateEdittext.text.toString()

            // validasi input field
            if (projectName.isEmpty() || taskName.isEmpty() || assignTo.isEmpty() || sprintEditText.isEmpty() || startDateEditText.isEmpty() || endDateEditText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val sprint = sprintEditText.toInt()

            // membuat formatter untuk tanggal
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            // mengubah tanggal dari string ke date
            val startDate = Calendar.getInstance().apply {
                time = dateFormatter.parse(startDateEditText) as Date
            }.time

            val endDate = Calendar.getInstance().apply {
                time = dateFormatter.parse(endDateEditText) as Date
            }.time

            // menyimpan data ke database
            databaseHandler.addData(
                Project(
                    0, projectName, taskName, assignTo, sprint, startDate, endDate, attachment
                )
            )

            // kembali ke halaman dashboard
            parentFragmentManager.beginTransaction().apply {
                (activity as MainActivity).replaceFragment(DashboardFragment())
                addToBackStack(null)
                commit()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // set selected date to calendar
        calendar.set(year, month, dayOfMonth)

        // format selected date and set to selected date text field
        selectedDateEditText.setText(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        )
    }

}