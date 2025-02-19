package com.GDG.Festi.domain;

import com.GDG.Festi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
