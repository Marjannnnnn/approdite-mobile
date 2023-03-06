package com.marjannnnn.approdite

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    private lateinit var projectRecyclerView: RecyclerView
    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var fab: FloatingActionButton
    private lateinit var database: FirebaseDatabase
    private lateinit var projectList: MutableList<Project>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progress_bar)
        fab = view.findViewById(R.id.fab)

        setupRecyclerView()
        setupFirebase()

        fab.setOnClickListener {
            showAddProjectDialog()
        }
    }

    private fun setupRecyclerView() {
        projectRecyclerView = requireView().findViewById(R.id.project_list)
        projectList = mutableListOf()
        projectAdapter = ProjectAdapter(projectList)
        projectRecyclerView.adapter = projectAdapter
        projectRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupFirebase() {
        FirebaseApp.initializeApp(requireContext())
        database = FirebaseDatabase.getInstance()

        val projectsRef = database.getReference("projects")
        projectsRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                projectList.clear()

                for (projectSnapshot in snapshot.children) {
                    val project = projectSnapshot.getValue(Project::class.java)
                    project?.let {
                        projectList.add(it)
                    }
                }
                projectAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to read project data", Toast.LENGTH_SHORT)
                    .show()
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun showAddProjectDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_layout, null)
        val projectEditText = dialogView.findViewById<EditText>(R.id.project_name_edittext)
        val taskEditText = dialogView.findViewById<EditText>(R.id.task_name_edittext)

        val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
        dialogBuilder.setView(dialogView)
        dialogBuilder.setPositiveButton("Submit") { dialog, which ->
            val projectName = projectEditText.text.toString()
            val taskName = taskEditText.text.toString()

            addProjectToFirebase(projectName, taskName)
        }

        dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun addProjectToFirebase(projectName: String, taskName: String) {
        val rootNode = FirebaseDatabase.getInstance().reference
        val projectsRef = rootNode.child("projects")
        val projectId = projectsRef.push().key
        val projectData = hashMapOf(
            "projectName" to projectName, "taskName" to taskName
        )

        projectId?.let {
            val task = projectsRef.child(it).setValue(projectData)
            task.addOnSuccessListener {
                Toast.makeText(
                    requireContext(), "Project added successfully!", Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(), "Error adding project: ${it.message}", Toast.LENGTH_LONG
                ).show()
            }
        }
    }


}