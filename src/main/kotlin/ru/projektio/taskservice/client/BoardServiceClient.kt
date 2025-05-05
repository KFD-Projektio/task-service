package ru.projektio.taskservice.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "board-service")
interface BoardServiceClient {
    @GetMapping("/internal/columns/{columnId}")
    fun getColumnInfo(@PathVariable columnId: Long): ResponseEntity<ColumnDataResponse>

    @GetMapping("/internal/boards/{boardId}")
    fun getBoardInfo(@PathVariable boardId: Long): ResponseEntity<BoardDataResponse>
}