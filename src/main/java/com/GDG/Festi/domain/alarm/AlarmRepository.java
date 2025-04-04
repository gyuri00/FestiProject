package com.GDG.Festi.domain.alarm;

import com.GDG.Festi.entity.Alarm;
import com.GDG.Festi.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByUser(Users user);
}
