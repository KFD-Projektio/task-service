package ru.projektio.taskservice.database.repository

import org.springframework.data.repository.CrudRepository
import ru.projektio.taskservice.database.entity.TaskEntity

interface TaskDao : CrudRepository<TaskEntity, Long> {
    fun findAllByColumnId(columnId: Long): MutableList<TaskEntity>

}