package com.marjannnnn.approdite.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.marjannnnn.approdite.MainActivity
import com.marjannnnn.approdite.databinding.FragmentEditProjectBinding
import com.marjannnnn.approdite.db.DatabaseHandler
import com.marjannnnn.approdite.model.Project
import java.text.SimpleDateFormat
import java.util.*

class EditProjectFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentEditProjectBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var datePicker: DatePickerDialog
    private lateinit var selectedDateEditText: TextInputEditText
    private lateinit var project: Project
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProjectBinding.inflate(inflater, container, false)
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

        project = requireArguments().getSerializable("project") as Project

        binding.projectNameEdittext.setText(project.projectName)
        binding.taskNameEdittext.setText(project.taskName)
        binding.assignToEdittext.setText(project.assignTo)
        binding.sprintEdittext.setText(project.sprint.toString())
        binding.attachmentEdittext.setText(project.attachment)
        binding.startDateEdittext.setText(
            SimpleDateFormat(
                "dd/MM/yyyy", Locale.getDefault()
            ).format(project.startDate)
        )
        binding.endDateEdittext.setText(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                project.endDate
            )
        )

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

        binding.editButton.setOnClickListener {
            val projectName = binding.projectNameEdittext.text.toString()
            val taskName = binding.taskNameEdittext.text.toString()
            val assignTo = binding.assignToEdittext.text.toString()
            val sprint = binding.sprintEdittext.text.toString().toInt()
            val attachment = binding.attachmentEdittext.text.toString()
            val startDateEditText = binding.startDateEdittext.text.toString()
            val endDateEditText = binding.endDateEdittext.text.toString()

            if (projectName.isEmpty() || taskName.isEmpty() || assignTo.isEmpty() || startDateEditText.isEmpty() || endDateEditText.isEmpty()) {
                // menampilkan pesan error jika ada input field yang kosong
                Toast.makeText(requireContext(), "Please fill all the fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val startDate = Calendar.getInstance().apply {
                time = dateFormatter.parse(startDateEditText) as Date
            }.time

            val endDate = Calendar.getInstance().apply {
                time = dateFormatter.parse(endDateEditText) as Date
            }.time

            databaseHandler.updateData(
                Project(
                    project.id,
                    projectName,
                    taskName,
                    assignTo,
                    sprint,
                    startDate,
                    endDate,
                    attachment
                )
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

    override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(Calendar.YEAR, year)
        selectedDate.set(Calendar.MONTH, monthOfYear)
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        selectedDateEditText.setText(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                selectedDate.time
            )
        )
    }

}