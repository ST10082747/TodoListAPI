package com.example.todolistapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(private val todos: List<Todo>, private val onDelete: (String) -> Unit, private val onUpdate: (Todo) -> Unit) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.todo_title)
        val statusTextView: TextView = itemView.findViewById(R.id.todo_status)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
        val updateButton: Button = itemView.findViewById(R.id.update_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]
        holder.titleTextView.text = todo.title
        holder.statusTextView.text = if (todo.completed) "Completed" else "Pending"


        holder.deleteButton.setOnClickListener {
            onDelete(todo.id)
        }

        holder.updateButton.setOnClickListener {
            onUpdate(todo)
        }
    }

    override fun getItemCount(): Int = todos.size
}
