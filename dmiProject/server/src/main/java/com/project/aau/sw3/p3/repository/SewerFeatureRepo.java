package com.project.aau.sw3.p3.repository;

import com.project.aau.sw3.p3.model.SewerFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SewerFeatureRepo extends JpaRepository<SewerFeature, Long> {
}
