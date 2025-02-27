package com.francislainy.sobe.repository;

import com.francislainy.sobe.entity.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<AnswerEntity, UUID> {

    List<AnswerEntity> findAllByQuestionEntity_Id(UUID questionId);
}
