package com.example.smartresume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.smartresume.model.FeedbackData;

public interface FeedbackRepository extends JpaRepository<FeedbackData, Long> {
    List<FeedbackData> findByResumeId(Long resumeId);
    Optional<FeedbackData> findByResumeIdAndJobId(Long resumeId, Long jobId);
    List<FeedbackData> findByResumeIdIn(List<Long> resumeIds); // for analytics
}