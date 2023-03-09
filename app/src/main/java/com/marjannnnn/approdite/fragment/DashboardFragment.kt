package com.marjannnnn.approdite.fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marjannnnn.approdite.MainActivity
import com.marjannnnn.approdite.R
import com.marjannnnn.approdite.adapter.ProjectAdapter
import com.marjannnnn.approdite.db.DatabaseHandler
import com.marjannnnn.approdite.model.Project

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

        fab = view.findViewById(R.id.fab)
        projectList = view.findViewById(R.id.project_list)

        fab.setOnClickListener {
            (activity as MainActivity).replaceFragment(FormProjectFragment())
//            showAddProjectDialog()
        }

        // get all project data from database
        val dbHandler = DatabaseHandler(requireContext())
        val projectDataList = dbHandler.getAllData()

        // set up RecyclerView adapter
        val adapter = ProjectAdapter(projectDataList, this)
        projectList.adapter = adapter
        projectList.layoutManager = LinearLayoutManager(requireContext())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showAddProjectDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_layout, null)
        val projectEditText = dialogView.findViewById<EditText>(R.id.project_name_edittext)
        val taskEditText = dialogView.findViewById<EditText>(R.id.task_name_edittext)

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.setView(dialogView)
        dialogBuilder.setPositiveButton("Submit", null)

        dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()

        // Set custom OnClickListener for the Submit button
        val submitButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
        submitButton.setOnClickListener {
            val projectName = projectEditText.text.toString().trim()
            val taskName = taskEditText.text.toString().trim()

            // Validate project and task names
            if (projectName.isEmpty()) {
                projectEditText.error = "Project name cannot be empty"
                return@setOnClickListener
            }
            if (taskName.isEmpty()) {
                taskEditText.error = "Task name cannot be empty"
                return@setOnClickListener
            }

            val dbHandler = DatabaseHandler(requireContext())
//            dbHandler.addData(projectName, taskName)
            val projectDataList = dbHandler.getAllData()
            (projectList.adapter as ProjectAdapter).setData(projectDataList)

            // Dismiss the dialog
            dialog.dismiss()
        }
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

        // set text to the TextViews
        projectNameTextView.text = "Project Name: ${project.projectName}"
        taskNameTextView.text = "Task Name: ${project.taskName}"
        assignToTextView.text = "Assign To: ${project.assignTo}"
        sprintTextView.text = "Sprint: ${project.sprint}"
        startDateTextView.text = "Start Date: ${project.startDate}"
        endDateTextView.text = "End Date: ${project.endDate}"
        attachmentTextView.text = "Attachment: ${project.attachment}"


        dialogBuilder.setView(dialogView)
        dialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onEditClick(project: Project) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_layout, null)
        val projectEditText = dialogView.findViewById<EditText>(R.id.project_name_edittext)
        val taskEditText = dialogView.findViewById<EditText>(R.id.task_name_edittext)

        // set default value of project name and task name
        projectEditText.setText(project.projectName)
        taskEditText.setText(project.taskName)

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.setView(dialogView)
        dialogBuilder.setPositiveButton("Update") { dialog, which ->
            val projectName = projectEditText.text.toString()
            val taskName = taskEditText.text.toString()
            val dbHandler = DatabaseHandler(requireContext())
            // update project data
//            dbHandler.updateData(Project(project.id, projectName, taskName))
            val projectDataList = dbHandler.getAllData()
            (projectList.adapter as ProjectAdapter).setData(projectDataList)
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onDeleteClick(project: Project) {
        val dbHandler = DatabaseHandler(requireContext())
        dbHandler.deleteData(project.id)
        val projectDataList = dbHandler.getAllData()
        (projectList.adapter as ProjectAdapter).setData(projectDataList)
    }
}