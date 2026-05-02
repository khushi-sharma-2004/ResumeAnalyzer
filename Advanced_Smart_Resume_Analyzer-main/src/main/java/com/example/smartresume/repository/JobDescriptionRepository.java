package com.example.smartresume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.smartresume.model.JobDescription;

@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, Long> {
}
