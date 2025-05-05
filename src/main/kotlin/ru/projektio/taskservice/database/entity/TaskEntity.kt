package ru.projektio.taskservice.database.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "tasks")
class TaskEntity(
    @Column(name = "title",nullable = false)
    var title: String,

    @Column(name = "description", nullable = true)
    var description: String?,

    @Column(name = "board_id", nullable = false)
    val boardId: Long,

    @Column(name = "column_id", nullable = false)
    val columnId: Long,

    @Column(name = "assignee_id", nullable = true)
    var assigneeId: Long?,

    @Column(name = "created_by", nullable = false)
    val createdBy: Long,

    @Column(name = "position", nullable = false)
    val position: Int,

    @Column(name = "deadline" , nullable = true)
    val deadline: LocalDateTime?
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
