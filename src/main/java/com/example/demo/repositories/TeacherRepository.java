package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.demo.entities.Teacher;

@RepositoryRestResource(path = "teachers")
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
}
