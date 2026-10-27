package com.example.spo.controller;

import com.example.spo.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:3000") // Reactからの通信を許可
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/reschedule")
    public ResponseEntity<String> rescheduleTasks() {
        // 実際の運用では現在の日付を取得
        LocalDate today = LocalDate.now();
        taskService.autoReschedule(today);
        return ResponseEntity.ok("スケジュールの自動再調整が完了しました。");
    }
}