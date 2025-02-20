package com.GDG.Festi.entity;

import com.GDG.Festi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
