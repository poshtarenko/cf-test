package com.poshtarenko.codeforge.repository;

import com.poshtarenko.codeforge.entity.test.Answer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @EntityGraph(attributePaths = {"respondent", "solutions", "solutions.task", "solutions.task.problem"})
    Optional<Answer> findById(long id);

    @EntityGraph(attributePaths = {"respondent", "solutions", "solutions.task", "solutions.task.problem"})
    Optional<Answer> findByRespondentIdAndTestInviteCode(Long respondentId, String inviteCode);

    @EntityGraph(attributePaths = {"respondent", "solutions", "solutions.task", "solutions.task.problem"})
    List<Answer> findAllByRespondentIdAndTestIdOrderByCreatedAtDesc(long respondentId, long testId);

    boolean existsByRespondentIdAndTestId(long respondentId, long testId);

    @EntityGraph(attributePaths = {"respondent", "solutions", "solutions.task", "solutions.task.problem"})
    List<Answer> findAllByTestId(long testId);

}

class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
