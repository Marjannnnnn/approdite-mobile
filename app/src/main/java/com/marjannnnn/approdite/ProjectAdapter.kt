package com.marjannnnn.approdite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProjectAdapter(
    var projects: List<Project>
) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {
    override fun getItemCount() = projects.size

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

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.numberTextView.text = (position + 1).toString()
        holder.projectNameTextView.text = project.projectName
        holder.taskNameTextView.text = project.taskName
    }

    fun updateProjects(newProjects: List<Project>) {
        projects = newProjects
        notifyDataSetChanged()
    }
}
