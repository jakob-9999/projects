package com.project.aau.sw3.p3.repository;

import com.project.aau.sw3.p3.model.GridCell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GridRepo extends JpaRepository<GridCell, Long> {

    //method for finding timesteps in database
    @Query("SELECT DISTINCT g.timeStep FROM GridCell g ORDER BY g.timeStep ASC")
    List<LocalDateTime> findAllTimeSteps();
}