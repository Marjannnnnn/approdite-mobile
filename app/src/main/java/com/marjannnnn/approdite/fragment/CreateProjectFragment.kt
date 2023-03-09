package com.marjannnnn.approdite.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.google.android.material.textfield.TextInputEditText
import com.marjannnnn.approdite.MainActivity
import com.marjannnnn.approdite.databinding.FragmentCreateProjectBinding
import com.marjannnnn.approdite.db.DatabaseHandler
import java.text.SimpleDateFormat
import java.util.*

class CreateProjectFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentCreateProjectBinding? = null
    private val binding get() = _binding!!

    private lateinit var datePicker: DatePickerDialog
    private lateinit var selectedDateEditText: TextInputEditText
    private val calendar = Calendar.getInstance()

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

        datePicker = DatePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        binding.startDateEdittext.setOnClickListener {
            selectedDateEditText = binding.startDateEdittext
            datePicker.show()
        }

        binding.endDateEdittext.setOnClickListener {
            selectedDateEditText = binding.endDateEdittext
            datePicker.show()
        }

        binding.createButton.setOnClickListener {
            val projectName = binding.projectNameEdittext.text.toString()
            val taskName = binding.taskNameEdittext.text.toString()
            val assignTo = binding.assignToEdittext.text.toString()
            val sprint = binding.sprintEdittext.text.toString().toInt()
            val attachment = binding.attachmentEdittext.text.toString()
            val startDateEditText = binding.startDateEdittext.text.toString()
            val endDateEditText = binding.endDateEdittext.text.toString()

            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val startDate = Calendar.getInstance().apply {
                time = dateFormatter.parse(startDateEditText) as Date
            }.time

            val endDate = Calendar.getInstance().apply {
                time = dateFormatter.parse(endDateEditText) as Date
            }.time

            databaseHandler.addData(
                projectName, taskName, assignTo, sprint, startDate, endDate, attachment
            )

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
        calendar.set(year, month, dayOfMonth)
        selectedDateEditText.setText(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                calendar.time
            )
        )
    }

}