package com.example.spo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class StudyTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;          // 例: "SPI 史上最強 推論"
    private String category;       // 例: "SPI", "Java", "玉手箱"
    private LocalDate scheduledDate; // 予定日
    private boolean isCompleted;   // 完了フラグ
    private int requiredMinutes;   // 所要時間（分）

    // Getters and Setters は省略（Lombokの@Data推奨）
}