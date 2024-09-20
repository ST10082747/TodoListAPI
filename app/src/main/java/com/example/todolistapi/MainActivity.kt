package com.example.todolistapi

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private lateinit var titleInput: EditText
    private lateinit var statusCheckbox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        titleInput = findViewById(R.id.todo_title_input)
        statusCheckbox = findViewById(R.id.todo_status_checkbox)

        val addButton: Button = findViewById(R.id.add_todo_button)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchTodos()

        addButton.setOnClickListener {
            val title = titleInput.text.toString()
            val newTodo = Todo(
                id = "",
                title = title,
                completed = statusCheckbox.isChecked
            )
            createTodo(newTodo)
        }
    }

    private fun fetchTodos() {
        RetrofitClient.apiService.getTodos().enqueue(object : Callback<List<Todo>> {
            override fun onResponse(call: Call<List<Todo>>, response: Response<List<Todo>>) {
                if (response.isSuccessful) {
                    val todos = response.body() ?: emptyList()


                    todoAdapter = TodoAdapter(todos,
                        onDelete = { id -> deleteTodo(id) },
                        onUpdate = { todo -> showUpdateDialog(todo) }
                    )
                    recyclerView.adapter = todoAdapter
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch todos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun createTodo(todo: Todo) {
        RetrofitClient.apiService.createTodo(todo).enqueue(object : Callback<Todo> {
            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                if (response.isSuccessful) {
                    fetchTodos()
                    Toast.makeText(this@MainActivity, "Todo added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to add todo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Todo>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteTodo(id: String) {
        RetrofitClient.apiService.deleteTodo(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    fetchTodos()
                    Toast.makeText(this@MainActivity, "Todo deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to delete todo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateTodo(id: String, todo: Todo) {
        RetrofitClient.apiService.updateTodo(id, todo).enqueue(object : Callback<Todo> {
            override fun onResponse(call: Call<Todo>, response: Response<Todo>) {
                if (response.isSuccessful) {
                    fetchTodos()
                    Toast.makeText(this@MainActivity, "Todo updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Failed to update todo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Todo>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun showUpdateDialog(todo: Todo) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_todo, null)
        val titleInput: EditText = dialogView.findViewById(R.id.title_input)
        val completedCheckbox: CheckBox = dialogView.findViewById(R.id.completed_checkbox)

        titleInput.setText(todo.title)
        completedCheckbox.isChecked = todo.completed

        AlertDialog.Builder(this)
            .setTitle("Update Todo")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedTitle = titleInput.text.toString()
                val isCompleted = completedCheckbox.isChecked

                val updatedTodo = Todo(
                    id = todo.id,
                    title = updatedTitle,
                    completed = isCompleted
                )
                updateTodo(todo.id, updatedTodo)
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}