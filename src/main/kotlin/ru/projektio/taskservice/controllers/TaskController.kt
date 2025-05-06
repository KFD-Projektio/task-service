package ru.projektio.taskservice.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.projektio.taskservice.dto.request.CreateTaskDtoRequest
import ru.projektio.taskservice.dto.request.GetTasksData
import ru.projektio.taskservice.dto.response.CreateTaskDtoResponse
import ru.projektio.taskservice.dto.response.TaskInfoResponse
import ru.projektio.taskservice.service.TaskService

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService,
) {
    @GetMapping
    fun getTasks(@RequestHeader("X-User-Id") userId: Long, @RequestBody getTasksData: GetTasksData): ResponseEntity<List<TaskInfoResponse>> =
        ResponseEntity.status(HttpStatus.OK).body(taskService.getTasks(userId, getTasksData))

    @PostMapping
    fun createTask(@RequestHeader("X-User-Id") userId: Long, @RequestBody taskData: CreateTaskDtoRequest) : ResponseEntity<CreateTaskDtoResponse>
    = ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(userId, taskData))
}