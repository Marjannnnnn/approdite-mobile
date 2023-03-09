package com.marjannnnn.approdite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marjannnnn.approdite.R
import com.marjannnnn.approdite.model.Project


class ProjectAdapter(
    private var projectDataList: List<Project>, private val listener: OnItemClickListener
) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onEditClick(project: Project)
        fun onDeleteClick(project: Project)
        fun onItemClick(project: Project)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.project_item_layout, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val projectData = projectDataList[position]
        holder.projectNumber.text = (position + 1).toString()
        holder.projectName.text = projectData.projectName
        holder.taskName.text = projectData.taskName

        holder.editBtn.setOnClickListener {
            listener.onEditClick(projectData)
        }

        holder.deleteBtn.setOnClickListener {
            listener.onDeleteClick(projectData)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newProjectList: List<Project>) {
        projectDataList = newProjectList
        notifyDataSetChanged()
    }

    override fun getItemCount() = projectDataList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectNumber: TextView = itemView.findViewById(R.id.project_number)
        val projectName: TextView = itemView.findViewById(R.id.project_name)
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val editBtn: ImageButton = itemView.findViewById(R.id.edit_btn)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.delete_btn)

        init {
            editBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEditClick(projectDataList[position])
                }
            }

            deleteBtn.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(projectDataList[position])
                }
            }

            itemView.setOnClickListener {
                listener.onItemClick(projectDataList[adapterPosition])
            }
        }
    }
}