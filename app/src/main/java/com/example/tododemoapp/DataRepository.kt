package com.example.tododemoapp

class DataRepository() {
    var taskList = ArrayList<Task>()

    fun addTask(task: Task) {
        taskList.add(task);
    }

    fun deleteTask(task: Task) {
        taskList.remove(task)
    }
}