package com.marjannnnn.approdite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var fab: FloatingActionButton
    private lateinit var projectRecyclerView: RecyclerView

    private lateinit var projectAdapter: ProjectAdapter
    private lateinit var projectList: MutableList<Project>

    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())

        database = FirebaseDatabase.getInstance()
        projectList = mutableListOf()
        projectAdapter = ProjectAdapter(projectList)

        // Read data from Firebase and update RecyclerView
        val projectsRef = database.getReference("projects")
        projectsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                projectList.clear()

                for (projectSnapshot in snapshot.children) {
                    val project = projectSnapshot.getValue(Project::class.java)
                    if (project != null) {
                        projectList.add(project)
                    }
                }

                projectAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to read project data", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab = requireView().findViewById(R.id.fab)

        fab.setOnClickListener {
            val dialogView =
                LayoutInflater.from(requireContext()).inflate(R.layout.dialog_layout, null)
            val projectEditText = dialogView.findViewById<EditText>(R.id.project_name_edittext)
            val taskEditText = dialogView.findViewById<EditText>(R.id.task_name_edittext)

            val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
            dialogBuilder.setView(dialogView)
            dialogBuilder.setPositiveButton("Submit") { dialog, which ->
                val projectName = projectEditText.text.toString()
                val taskName = taskEditText.text.toString()

                // Get reference to the root node of Realtime Database
                val rootNode = FirebaseDatabase.getInstance().reference

                // Get reference to "projects" node
                val projectsRef = rootNode.child("projects")

                // Generate unique key for this project
                val projectId = projectsRef.push().key

                // Create HashMap object to store project data
                val projectData = HashMap<String, Any>()
                projectData["projectName"] = projectName
                projectData["taskName"] = taskName

                // Add project data to "projects" node
                val task = projectsRef.child(projectId!!).setValue(projectData)

                // Show success or error message
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

            dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = dialogBuilder.create()
            dialog.show()
        }

        projectAdapter.setOnItemClickListener(object : ProjectAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
                dialogBuilder.setMessage("Delete this project?")
                dialogBuilder.setPositiveButton("Yes") { dialog, which ->
                    val projectId = projectAdapter.projects[position].id
//                    projectAdapter.deleteProject(position) error
                    FirebaseDatabase.getInstance().reference.child("projects").child(projectId!!).removeValue()
                    Toast.makeText(requireContext(), "Project deleted", Toast.LENGTH_SHORT).show()
                }
                dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                val dialog = dialogBuilder.create()
                dialog.show()
            }
        })

        projectRecyclerView = requireView().findViewById(R.id.project_list)
        projectRecyclerView.adapter = projectAdapter
        projectRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = DashboardFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}