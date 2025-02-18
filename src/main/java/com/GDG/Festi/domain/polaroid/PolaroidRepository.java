package com.GDG.Festi.domain.polaroid;

import com.GDG.Festi.entity.Polaroid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolaroidRepository extends JpaRepository<Polaroid, Long> {
}
