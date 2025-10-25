package com.project.aau.sw3.p3.repository;

import com.project.aau.sw3.p3.model.SewerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//JpaRepository is a generic class from Spring Data JPA
//SewerData tells us which entity/tabel we're working with
//Long is the type of the primary key
//query not necessary for saving data

//JpaRepository enables methods such as:
//save(entity) saves or updates row
//findAll() gets all rows
//findById(id) gets one row
//delete(entity) deletes one row

@Repository
public interface SewerDataRepo extends JpaRepository<SewerData, Long> {
}


