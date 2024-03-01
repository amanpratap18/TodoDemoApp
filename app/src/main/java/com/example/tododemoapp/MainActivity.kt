package com.example.tododemoapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var dataRepository: DataRepository
    private var taskTitle = ""
    private lateinit var recyclerview: RecyclerView
    private lateinit var emptyTaskCard: CardView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview = findViewById(R.id.rvTaskTitle)
        emptyTaskCard = findViewById(R.id.cvEmptyTask)
        dataRepository = DataRepository()
        setAdapter()
        checkCardVisibility()

    }

    private fun checkCardVisibility() {
        if (isTaskNotEmpty()) {
            emptyTaskCard.visibility = View.VISIBLE
        } else {
            emptyTaskCard.visibility = View.GONE
        }
    }

    private fun setAdapter() {
        recyclerview.layoutManager = LinearLayoutManager(this)
        val adapter = TaskAdapter(dataRepository.taskList, object : TaskInterface {
            override fun onClickListener(position: Int, task: Task) {
                Log.d("TASK", "item position======= $position")
                dataRepository.deleteTask(task)
                checkCardVisibility()
            }
        })
        recyclerview.adapter = adapter
        clickListeners(adapter)
    }

    private fun clickListeners(adapter: TaskAdapter) {
        val addTaskBtn = findViewById<FloatingActionButton>(R.id.btnAddTask)
        addTaskBtn.setOnClickListener {
            showDialog(adapter)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDialog(adapter: TaskAdapter) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add Task")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.hint = "Enter Task Name"
        builder.setView(input)

        builder.setPositiveButton(
            "Add"
        ) { _, _ ->
            taskTitle = input.text.toString()
            var taskId = 1
            if (taskTitle.trim().isNotEmpty()) {
                dataRepository.addTask(Task(++taskId, taskTitle))
                checkCardVisibility()
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(
                    this,
                    "OOPs!! Empty Task,Please enter task name..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun isTaskNotEmpty(): Boolean {
        return dataRepository.taskList.isEmpty()
    }


}