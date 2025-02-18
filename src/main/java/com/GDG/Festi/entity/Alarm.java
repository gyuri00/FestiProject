package com.GDG.Festi.entity;

import com.GDG.Festi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alarm")
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String alarmMsg;
    private LocalDateTime sendTime;
    private String alarmType;
    private Boolean isRead;
    private String linkPath;
}
