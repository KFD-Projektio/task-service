package ru.projektio.taskservice.service.impl

import jakarta.transaction.Transactional
import jakarta.ws.rs.core.NoContentException
import org.springframework.stereotype.Service
import ru.projektio.taskservice.client.BoardServiceClient
import ru.projektio.taskservice.database.entity.BoardTaskEntity
import ru.projektio.taskservice.database.entity.TaskEntity
import ru.projektio.taskservice.database.repository.BoardTaskDao
import ru.projektio.taskservice.database.repository.TaskDao
import ru.projektio.taskservice.dto.request.CreateTaskDtoRequest
import ru.projektio.taskservice.dto.request.UpdateTaskRequest
import ru.projektio.taskservice.dto.response.CreateTaskDtoResponse
import ru.projektio.taskservice.dto.response.TaskInfoResponse
import ru.projektio.taskservice.exception.BoardClientException
import ru.projektio.taskservice.exception.RestrictedUserException
import ru.projektio.taskservice.service.TaskService
import java.time.LocalDateTime

@Service
class TaskServiceImpl(
    private val taskDao: TaskDao,
    private val boardServiceClient: BoardServiceClient,
    private val boardTaskDao: BoardTaskDao
) : TaskService {

    override fun getTasks(userId: Long, boardId: Long): List<TaskInfoResponse> {
        val boardInfo = getBoardInfoOrThrow(boardId)
        checkUserAccess(userId, boardInfo.userIds)
        return taskDao.findAllByBoardId(boardInfo.id).map(TaskInfoResponse::from)
    }

    @Transactional
    override fun createTask(userId: Long, taskData: CreateTaskDtoRequest): CreateTaskDtoResponse {
        val boardInfo = getBoardInfoOrThrow(taskData.boardId)
        checkUserAccess(userId, boardInfo.userIds)
        checkColumnAccess(taskData.columnId, boardInfo.columnsIds)

        val task = TaskEntity(
            title = taskData.title,
            description = taskData.description,
            boardId = taskData.boardId,
            columnId = taskData.columnId,
            assigneeId = taskData.assigneeId,
            position = boardTaskDao.findAllByBoardId(taskData.boardId).size,
            createdBy = userId,
            deadline = LocalDateTime.now()
        )

        taskDao.save(task)
        boardTaskDao.save(BoardTaskEntity(taskId = task.id, boardId = task.boardId))

        return CreateTaskDtoResponse.from(task)
    }

    @Transactional
    override fun updateTask(userId: Long, taskId: Long, taskDataToUpdate: UpdateTaskRequest): TaskInfoResponse {
        val task = getTaskOrThrow(taskId)
        val boardInfo = getBoardInfoOrThrow(task.boardId)
        checkUserAccess(userId, boardInfo.userIds)

        task.title = taskDataToUpdate.title ?: task.title
        task.description = taskDataToUpdate.description ?: task.description
        task.assigneeId = taskDataToUpdate.assigneeId ?: task.assigneeId

        taskDao.save(task)
        return TaskInfoResponse.from(task)
    }

    @Transactional
    override fun deleteTask(userId: Long, taskId: Long) {
        val task = getTaskOrThrow(taskId)
        val boardInfo = getBoardInfoOrThrow(task.boardId)
        checkUserAccess(userId, boardInfo.userIds)
        taskDao.delete(task)
    }

    private fun getBoardInfoOrThrow(boardId: Long) =
        boardServiceClient.getBoardInfo(boardId).body ?: throw BoardClientException("Request Error")

    private fun getTaskOrThrow(taskId: Long) =
        taskDao.findById(taskId).orElseThrow { NoContentException("Task not found") }

    private fun checkUserAccess(userId: Long, userIds: List<Long>) {
        if (userId !in userIds) {
            throw RestrictedUserException("User not allowed")
        }
    }

    private fun checkColumnAccess(columnId: Long, columnIds: List<Long>) {
        if (columnId !in columnIds) {
            throw BoardClientException("Invalid column for the board")
        }
    }
}
