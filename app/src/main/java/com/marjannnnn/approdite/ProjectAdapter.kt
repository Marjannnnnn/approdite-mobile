package com.marjannnnn.approdite

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ProjectAdapter(
    var projects: List<Project>,
) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {
    override fun getItemCount() = projects.size

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectNameTextView: TextView = itemView.findViewById(R.id.project_name)
        val taskNameTextView: TextView = itemView.findViewById(R.id.task_name)
        val numberTextView: TextView = itemView.findViewById(R.id.number)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.project_item_layout, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.numberTextView.text = (position + 1).toString()
        holder.projectNameTextView.text = project.projectName
        holder.taskNameTextView.text = project.taskName
        holder.deleteButton.setOnClickListener {
            deleteProject(project.id!!, holder.itemView.context)
        }
    }


    private fun deleteProject(projectId: Int, context: Context) {
        val rootNode = FirebaseDatabase.getInstance().reference
        val projectRef = rootNode.child("projects").child(projectId.toString())
//        Log.i("ingfo","$projectRef")
        projectRef.removeValue().addOnSuccessListener {
            Toast.makeText(
                context, "Project deleted successfully!", Toast.LENGTH_SHORT
            ).show()
        }.addOnFailureListener {
            Toast.makeText(
                context, "Error deleting project: ${it.message}", Toast.LENGTH_LONG
            ).show()
        }
    }
}
