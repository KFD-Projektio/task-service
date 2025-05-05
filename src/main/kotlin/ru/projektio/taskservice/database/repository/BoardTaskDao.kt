package ru.projektio.taskservice.database.repository

import org.springframework.data.repository.CrudRepository
import ru.projektio.taskservice.database.entity.BoardTaskEntity

interface BoardTaskDao : CrudRepository<BoardTaskEntity, Long> {
    fun findAllByBoardId(boardId: Long): MutableList<BoardTaskEntity>
}