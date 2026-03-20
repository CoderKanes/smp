package com.smp.repository;

import com.smp.entity.SMP_Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMP_AnswerRepository extends JpaRepository<SMP_Answer, Long> {

    // 직접 쿼리를 작성하면 JPA가 이름을 분석하지 않으므로 에러가 사라집니다.
    @Query("SELECT a FROM SMP_Answer a WHERE a.smp_Question.id = :questionId ORDER BY a.createdAt DESC")
    List<SMP_Answer> findAllByQuestionId(@Param("questionId") Long questionId);
}