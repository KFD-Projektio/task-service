package ru.projektio.taskservice.database.entity

import jakarta.persistence.*

@Entity
@Table(name = "BoardTasks")
class BoardTaskEntity(
    @Column(name = "board_id")
    val boardId: Long,

    @Column(name = "task_id")
    val taskId: Long,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
}