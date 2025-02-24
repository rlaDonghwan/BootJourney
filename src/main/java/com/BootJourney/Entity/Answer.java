package com.BootJourney.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;
    
    private LocalDateTime createDate;

    // Question과의 N:1 관계 설정
    @ManyToOne
    private Question question;

    @ManyToOne
    private User author;

    private LocalDateTime modifyDate;
}