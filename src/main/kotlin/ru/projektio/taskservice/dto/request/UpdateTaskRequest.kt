package ru.projektio.taskservice.dto.request

//
//data class CreateTaskDtoRequest (
//    val title: String,
//    val description: String,
//    val assigneeId: Long?,
//    val createdBy: Long,
//    val boardId: Long,
//    val columnId: Long
//)


data class UpdateTaskRequest (
    val title: String?,
    val description: String?,
    val assigneeId: Long?,
)
