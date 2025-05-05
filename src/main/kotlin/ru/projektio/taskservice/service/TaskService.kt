package ru.projektio.taskservice.service

import ru.projektio.taskservice.dto.request.CreateTaskDtoRequest
import ru.projektio.taskservice.dto.request.GetTasksData
import ru.projektio.taskservice.dto.request.UpdateTaskRequest
import ru.projektio.taskservice.dto.response.CreateTaskDtoResponse
import ru.projektio.taskservice.dto.response.TaskInfoResponse

interface TaskService {
    fun getTasks(userId: Long, getTaskRequest: GetTasksData): List<TaskInfoResponse>
    fun createTask(userId: Long, taskData: CreateTaskDtoRequest): CreateTaskDtoResponse
    fun updateTask(userId: Long, taskId: Long, taskDataToUpdate: UpdateTaskRequest): TaskInfoResponse
}
