package com.poshtarenko.codeforge.service;


import com.poshtarenko.codeforge.dto.request.CreateSolutionDTO;
import com.poshtarenko.codeforge.dto.request.TryCodeRequest;
import com.poshtarenko.codeforge.dto.response.ViewSolutionDTO;
import com.poshtarenko.codeforge.entity.test.TaskCompletionStatus;

public interface SolutionService {

    ViewSolutionDTO find(long id);

    ViewSolutionDTO put(CreateSolutionDTO answerDTO);

    TaskCompletionStatus tryCode(TryCodeRequest tryCodeRequest);

    void delete(long id);

    void checkAccess(long solutionId, long respondentId);
}
