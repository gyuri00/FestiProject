package com.GDG.Festi.entity;

import com.GDG.Festi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "polaroid")
public class Polaroid extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long polaroidId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    private String imgLink; // 폴라로이드 img 링크
}
