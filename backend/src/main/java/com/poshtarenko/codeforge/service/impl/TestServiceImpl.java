package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.TestMapper;
import com.poshtarenko.codeforge.dto.request.CreateTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;
import com.poshtarenko.codeforge.entity.test.Test;
import com.poshtarenko.codeforge.entity.user.Author;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.AuthorRepository;
import com.poshtarenko.codeforge.repository.TestRepository;
import com.poshtarenko.codeforge.service.TestService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('AUTHOR')")
public class TestServiceImpl implements TestService {

    private static final int TEST_INVITE_CODE_LENGTH = 8;

    private final TestRepository testRepository;
    private final AuthorRepository authorRepository;
    private final AnswerRepository answerRepository;
    private final TestMapper testMapper;

    @Override
    @PreAuthorize("hasAnyAuthority('AUTHOR', 'RESPONDENT')")
    public ViewTestDTO find(long id) {
        Test test = findById(id);
        return testMapper.toDto(test);
    }

    @Override
    public List<ViewTestDTO> findAuthorTests(long authorId) {
        return testRepository.findAllByAuthorId(authorId)
                .stream()
                .map(testMapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public ViewTestDTO save(Long userId, CreateTestDTO testDTO) {
        String code;
        do {
            code = RandomStringUtils.randomAlphabetic(TEST_INVITE_CODE_LENGTH);
        } while (testRepository.findByInviteCode(code).isPresent());
        Test test = testMapper.toEntity(testDTO);
        test.setAuthor(authorRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(Author.class, userId)));
        test.setInviteCode(code);
        return testMapper.toDto(testRepository.save(test));
    }

    @Override
    @Transactional
    public ViewTestDTO update(Long testId, UpdateTestDTO testDTO) {
        Test test = findById(testId);
        test.setName(testDTO.name());
        test.setMaxDuration(testDTO.maxDuration());
        return testMapper.toDto(testRepository.save(test));
    }

    @Override
    @Transactional
    public void delete(long id) {
        testRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("hasAuthority('RESPONDENT')")
    public void checkRespondentConnectedToTest(long respondentId, long testId) {
        if (!answerRepository.existsByRespondentIdAndTestId(respondentId, testId)) {
            throw new RuntimeException("Respondent is not connected to test");
        }
    }

    @Override
    public void checkAccess(long testId, long authorId) {
        Test test = findById(testId);
        if (!test.getAuthor().getId().equals(authorId)) {
            throw new EntityAccessDeniedException(Test.class, testId, authorId);
        }
    }

    private Test findById(long testId) {
        return testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException(Test.class, testId));
    }
}
