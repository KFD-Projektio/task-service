package ru.projektio.taskservice.dto.response

import ru.projektio.taskservice.database.entity.TaskEntity
import java.time.LocalDateTime

data class TaskInfoResponse (
    val id: Long,
    val title: String,
    val columnId: Long,
    val boardId: Long,
    val description: String?,
    val assigneeId: Long?,
    val createdBy: Long,
    val createdAt: LocalDateTime,
    val lastUpdatedAt: LocalDateTime,
    val dueDate: LocalDateTime?,
    val position: Int
) {
    companion object {
        fun from(taskEntity: TaskEntity): TaskInfoResponse {
            return TaskInfoResponse(
                taskEntity.id,
                taskEntity.title,
                taskEntity.columnId,
                taskEntity.boardId,
                taskEntity.description,
                taskEntity.assigneeId,
                taskEntity.createdBy,
                taskEntity.createdAt,
                taskEntity.updatedAt,
                taskEntity.deadline,
                taskEntity.position
            )
        }
    }
}
