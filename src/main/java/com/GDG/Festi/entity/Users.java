package com.GDG.Festi.entity;

import com.GDG.Festi.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class Users extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String password;
    private String username;
    private Boolean consent;
    private String studentId;

    private String provider;
    private String providerId;   // 소셜에서 받은 고유 식별자
    private String profileImageUrl;
    private String role;         // USER, ADMIN 등

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("this.role : " + this.role);
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}