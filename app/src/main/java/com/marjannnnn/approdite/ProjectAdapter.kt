package com.marjannnnn.approdite

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class ProjectAdapter(
    val projects: List<Project>
) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectNameTextView: TextView = itemView.findViewById(R.id.project_name)
        val taskNameTextView: TextView = itemView.findViewById(R.id.task_name)
        val numberTextView: TextView = itemView.findViewById(R.id.number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.project_item_layout, parent, false)
        return ProjectViewHolder(itemView)
    }

    // note: error delete
    fun deleteProject(position: Int) {
        val rootNode = FirebaseDatabase.getInstance().reference
        rootNode.child("projects")
    }

    override fun getItemCount() = projects.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.numberTextView.text = project.id
        holder.projectNameTextView.text = project.projectName
        holder.taskNameTextView.text = project.taskName
    }
}
