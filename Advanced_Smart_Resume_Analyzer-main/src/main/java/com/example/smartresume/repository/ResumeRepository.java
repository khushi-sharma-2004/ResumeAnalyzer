package com.example.smartresume.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.smartresume.model.ResumeData;
import com.example.smartresume.model.User;

public interface ResumeRepository extends JpaRepository<ResumeData, Long> {

    List<ResumeData> findByUser(User user);
    List<ResumeData> findByDeletedFalse();

    @Query("SELECT r FROM ResumeData r WHERE " +
           "LOWER(r.name) LIKE %:keyword% OR " +
           "LOWER(r.email) LIKE %:keyword% OR " +
           "LOWER(r.skillsText) LIKE %:keyword%")
    List<ResumeData> searchByKeyword(@Param("keyword") String keyword);

    List<ResumeData> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSkillsTextContainingIgnoreCase(
        String name, String email, String skillsText);
}