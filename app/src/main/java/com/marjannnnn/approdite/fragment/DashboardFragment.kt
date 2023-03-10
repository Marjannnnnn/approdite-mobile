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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marjannnnn.approdite.MainActivity
import com.marjannnnn.approdite.R
import com.marjannnnn.approdite.adapter.ProjectAdapter
import com.marjannnnn.approdite.db.DatabaseHandler
import com.marjannnnn.approdite.model.Project
import java.text.SimpleDateFormat
import java.util.Locale

class DashboardFragment : Fragment(), ProjectAdapter.OnItemClickListener {

    private lateinit var fab: FloatingActionButton
    private lateinit var projectList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        fab = view.findViewById(R.id.fab)
        projectList = view.findViewById(R.id.project_list)

        fab.setOnClickListener {
            (activity as MainActivity).replaceFragment(CreateProjectFragment())
        }

        // get all project data from database
        val dbHandler = DatabaseHandler(requireContext())
        val projectDataList = dbHandler.getAllData()

        // set up RecyclerView adapter
        val adapter = ProjectAdapter(projectDataList, this)
        projectList.adapter = adapter
        projectList.layoutManager = LinearLayoutManager(requireContext())
    }

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onItemClick(project: Project) {
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.setTitle("Detail Project")

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.detail_layout, null)
        val projectNameTextView = dialogView.findViewById<TextView>(R.id.project_name_textview)
        val taskNameTextView = dialogView.findViewById<TextView>(R.id.task_name_textview)
        val assignToTextView = dialogView.findViewById<TextView>(R.id.assign_to_textview)
        val sprintTextView = dialogView.findViewById<TextView>(R.id.sprint_textview)
        val startDateTextView = dialogView.findViewById<TextView>(R.id.start_date_textview)
        val endDateTextView = dialogView.findViewById<TextView>(R.id.end_date_textview)
        val attachmentTextView = dialogView.findViewById<TextView>(R.id.attachment_textview)
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        val startDate = outputFormat.format(project.startDate)
        val endDate = outputFormat.format(project.endDate)

        // set text to the TextViews
        projectNameTextView.text = "Project Name: ${project.projectName}"
        taskNameTextView.text = "Task Name: ${project.taskName}"
        assignToTextView.text = "Assign To: ${project.assignTo}"
        sprintTextView.text = "Sprint: ${project.sprint}"
        startDateTextView.text = "Start Date: $startDate"
        endDateTextView.text = "End Date: $endDate"
        attachmentTextView.text = "Attachment: ${project.attachment}"

        dialogBuilder.setView(dialogView)
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onEditClick(project: Project) {
        val args = Bundle().apply {
            putSerializable("project", project)
        }

        val editProjectFragment = EditProjectFragment().apply {
            arguments = args
        }
        (activity as MainActivity).replaceFragment(editProjectFragment)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDeleteClick(project: Project) {
        val dbHandler = DatabaseHandler(requireContext())

        AlertDialog.Builder(requireContext()).setTitle(
            "Confirm"
        ).setMessage("Are you sure you want to delete the '${project.projectName}' project?")
            .setPositiveButton("Yes") { dialog, _ ->
                dbHandler.deleteData(project.id)
                val projectDataList = dbHandler.getAllData()
                (projectList.adapter as ProjectAdapter).setData(projectDataList)
                dialog.dismiss()
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

}