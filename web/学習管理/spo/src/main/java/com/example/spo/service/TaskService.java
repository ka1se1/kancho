package com.example.spo.service;

import com.example.spo.model.StudyTask;
import com.example.spo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // 「今日の未完了タスク」を翌日に回し、今後の予定を調整するAI的機能
    @Transactional
    public void autoReschedule(LocalDate today) {
        // 今日終わっていないタスクを取得
        List<StudyTask> uncompletedTasks = taskRepository.findByScheduledDateAndIsCompletedFalse(today);
        
        if (uncompletedTasks.isEmpty()) {
            return; // 遅れなし！
        }

        // 未完了タスクを明日に移動
        LocalDate tomorrow = today.plusDays(1);
        for (StudyTask task : uncompletedTasks) {
            task.setScheduledDate(tomorrow);
            taskRepository.save(task);
        }

        // ※発展課題：明日のタスク量がキャパ（例: 1日4時間）を超えたら、
        // さらに明後日に玉突きで移動させるロジックをここに追加すると、
        // 「最適化アルゴリズムを自分で実装しました」と面接で語れます！
    }
}