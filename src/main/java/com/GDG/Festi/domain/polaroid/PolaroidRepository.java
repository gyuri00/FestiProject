package com.GDG.Festi.domain.polaroid;

import com.GDG.Festi.entity.Polaroid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolaroidRepository extends JpaRepository<Polaroid, Long> {
    @Query(value = "SELECT * FROM polaroid order by RAND() limit 20",nativeQuery = true)
    List<Polaroid> findRandomPolaroids();
}
