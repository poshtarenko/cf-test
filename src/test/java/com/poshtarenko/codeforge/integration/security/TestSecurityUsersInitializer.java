package com.poshtarenko.codeforge.integration.security;

import com.poshtarenko.codeforge.entity.Author;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.entity.Respondent;
import com.poshtarenko.codeforge.entity.User;
import com.poshtarenko.codeforge.repository.AuthorRepository;
import com.poshtarenko.codeforge.repository.RespondentRepository;
import com.poshtarenko.codeforge.repository.UserRepository;
import com.poshtarenko.codeforge.security.pojo.SignUpRequest;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsServiceImpl;
import com.poshtarenko.codeforge.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TestSecurityUsersInitializer {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final RespondentRepository respondentRepository;
    private final UserDetailsServiceImpl userDetailsService;

    private Map<ERole, UserDetailsImpl> userDetailsMap;

    @PostConstruct
    private void setup() {
        authorRepository.deleteAll();
        respondentRepository.deleteAll();
        userRepository.deleteAll();

        userDetailsMap = new HashMap<>();
        for (ERole role : ERole.values()) {
            userDetailsMap.put(role, registerUser(role));
        }
    }

    @PreDestroy
    private void clear() {
        authorRepository.deleteAll();
        respondentRepository.deleteAll();
        userRepository.deleteAll();
    }

    private UserDetailsImpl registerUser(ERole role) {
        String roleString = role.toString();

        String email = roleString + "@gmail.com";
        String password = "password";
        String name = "Some " + roleString + " name";

        SignUpRequest authorSignUpRequest = new SignUpRequest(
                email,
                password,
                name,
                role
        );

        userService.register(authorSignUpRequest);
        return (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
    }

    public UserDetailsImpl getUserDetails(ERole role) {
        return userDetailsMap.get(role);
    }

    public Author getAuthor() {
        Long id = userDetailsMap.get(ERole.AUTHOR).getId();
        return authorRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    public Respondent getRespondent() {
        Long id = userDetailsMap.get(ERole.RESPONDENT).getId();
        return respondentRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    public User getUser(ERole role) {
        Long id = userDetailsMap.get(role).getId();
        return userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
