package com.marjannnnn.approdite.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
            showAddProjectDialog()
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
        dialogBuilder.setPositiveButton("Submit") { dialog, which ->
            val projectName = projectEditText.text.toString()
            val taskName = taskEditText.text.toString()
            val dbHandler = DatabaseHandler(requireContext())
            dbHandler.addData(projectName, taskName)
            val projectDataList = dbHandler.getAllData()
            (projectList.adapter as ProjectAdapter).setData(projectDataList)
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onEditClick(project: Project) {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_layout, null)
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
            dbHandler.updateData(Project(project.id, projectName, taskName))
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
