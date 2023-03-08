package com.marjannnnn.approdite.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.google.android.material.textfield.TextInputEditText
import com.marjannnnn.approdite.R
import com.marjannnnn.approdite.adapter.ProjectAdapter
import com.marjannnnn.approdite.databinding.FragmentFormProjectBinding
import com.marjannnnn.approdite.db.DatabaseHandler
import com.marjannnnn.approdite.model.Project
import java.text.SimpleDateFormat
import java.util.*

class FormProjectFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentFormProjectBinding? = null
    private val binding get() = _binding!!

    private lateinit var datePicker: DatePickerDialog
    private lateinit var selectedDateEditText: TextInputEditText
    private val calendar = Calendar.getInstance()

    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormProjectBinding.inflate(inflater, container, false)
        databaseHandler = DatabaseHandler(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker = DatePickerDialog(
            requireContext(), this, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
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
            val startDate = calendar.time
            selectedDateEditText = binding.startDateEdittext
            val endDate = calendar.time
            selectedDateEditText = binding.endDateEdittext

            val project = Project(
                id = 1,
                projectName = projectName,
                taskName = taskName,
                assignTo = assignTo,
                sprint = sprint,
                attachment = attachment,
                startDate = startDate,
                endDate = endDate
            )


            databaseHandler.addData(project)

            val dashboardFragment = DashboardFragment()
            parentFragmentManager.beginTransaction().apply {
//                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                add(R.id.frame_layout, dashboardFragment)
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
        selectedDateEditText.setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time))
    }

}
