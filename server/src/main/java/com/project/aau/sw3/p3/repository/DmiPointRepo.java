package com.project.aau.sw3.p3.repository;

import com.project.aau.sw3.p3.model.DmiPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DmiPointRepo extends JpaRepository<DmiPoint, Long> {
}