package com.poshtarenko.codeforge.controller.http;

import com.poshtarenko.codeforge.dto.response.ViewAnswerDTO;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.service.AnswerService;
import com.poshtarenko.codeforge.service.TestService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/answers")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final TestService testService;

    @GetMapping("/{id}")
    public ViewAnswerDTO findAnswer(@PathVariable @Positive long id,
                                    @AuthenticationPrincipal UserDetailsImpl user) {
        answerService.checkAccess(id, user.getId());
        return answerService.find(id);
    }

    @GetMapping("/by_test/{testId}")
    public List<ViewAnswerDTO> findTestAnswers(@PathVariable @Positive long testId,
                                               @AuthenticationPrincipal UserDetailsImpl user) {
        testService.checkAccess(testId, user.getId());
        return answerService.findByTest(testId);
    }

    @GetMapping("/current/{testId}")
    public ViewAnswerDTO findRespondentCurrentAnswer(@PathVariable @Positive long testId,
                                                     @AuthenticationPrincipal UserDetailsImpl user) {
        Optional<ViewAnswerDTO> result = answerService.findRespondentCurrentAnswer(user.getId(), testId);
        return result.orElse(null);
    }

    @PostMapping("/start/{testCode}")
    public ViewAnswerDTO startAnswer(@PathVariable @Size(min = 8, max = 8) String testCode,
                                     @AuthenticationPrincipal UserDetailsImpl user) {
        return answerService.startAnswer(user.getId(), testCode);
    }

    @PostMapping("/finish/{answerId}")
    public ViewAnswerDTO finishAnswer(@PathVariable @Positive long answerId,
                                      @AuthenticationPrincipal UserDetailsImpl user) {
        answerService.checkAccess(answerId, user.getId());
        return answerService.finishAnswer(answerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable @Positive long id,
                                          @AuthenticationPrincipal UserDetailsImpl user) {
        answerService.checkAccess(id, user.getId());
        answerService.delete(id);
        return ResponseEntity.ok().build();
    }

}
