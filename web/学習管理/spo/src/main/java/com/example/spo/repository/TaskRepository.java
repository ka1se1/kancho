package com.example.spo.repository;

import com.example.spo.model.StudyTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<StudyTask, Long> {
    // 予定日が今日で、未完了のタスクを探すメソッド
    List<StudyTask> findByScheduledDateAndIsCompletedFalse(LocalDate date);
}