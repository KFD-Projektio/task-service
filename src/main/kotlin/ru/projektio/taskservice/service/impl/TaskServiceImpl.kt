package ru.projektio.taskservice.service.impl

//import org.springframework.transaction.annotation.Transactional
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
        val boardInfo = boardServiceClient.getBoardInfo(boardId).body ?: throw BoardClientException("Request Error")

        if (userId !in boardInfo.userIds) {
            throw RestrictedUserException("User not allowed")
        }

        return taskDao.findAllByBoardId(boardInfo.id).map { task -> TaskInfoResponse.from(task)}
    }


    @Transactional
    override fun createTask(userId:Long, taskData: CreateTaskDtoRequest): CreateTaskDtoResponse {
        val boardInfo = boardServiceClient.getBoardInfo(taskData.boardId).body ?: throw BoardClientException("Request Error")

        if (taskData.boardId != boardInfo.id || taskData.columnId !in boardInfo.columnsIds) {
            throw BoardClientException("Wrong board or column")
        }

        if (userId !in boardInfo.userIds) {
            throw RestrictedUserException("User not allowed")
        }


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

        val boardTaskEntity = BoardTaskEntity(
            taskId = task.id,
            boardId = task.boardId,
        )

        boardTaskDao.save(boardTaskEntity)
        taskDao.save(task)

        return CreateTaskDtoResponse.from(task)
    }

    @Transactional
    override fun updateTask(userId: Long, taskId: Long, taskDataToUpdate: UpdateTaskRequest): TaskInfoResponse {

        val task = taskDao.findById(taskId).orElseThrow { NoContentException("Task not found") }
        val boardInfo = boardServiceClient.getBoardInfo(task.boardId).body ?: throw BoardClientException("Request Error")

        if (userId !in boardInfo.userIds) {
            throw RestrictedUserException("User not allowed")
        }

        task.title = taskDataToUpdate.title ?: task.title
        task.description = taskDataToUpdate.description ?: task.description
        task.assigneeId = taskDataToUpdate.assigneeId ?: task.assigneeId

        taskDao.save(task)

        return TaskInfoResponse.from(task)
    }

    override fun deleteTask(userId: Long, taskId: Long): Unit {
        val task = taskDao.findById(taskId).orElseThrow { NoContentException("Task not found") }

        val boardInfo = boardServiceClient.getBoardInfo(task.boardId).body ?: throw BoardClientException("Request Error")

        if (userId !in boardInfo.userIds) {
            throw RestrictedUserException("User not allowed")
        }

        taskDao.delete(task)
    }
}