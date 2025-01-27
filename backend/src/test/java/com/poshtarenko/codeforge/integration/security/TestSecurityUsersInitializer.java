package com.poshtarenko.codeforge.integration.security;

import com.poshtarenko.codeforge.entity.user.Author;
import com.poshtarenko.codeforge.entity.user.ERole;
import com.poshtarenko.codeforge.entity.user.Respondent;
import com.poshtarenko.codeforge.entity.user.User;
import com.poshtarenko.codeforge.repository.AuthorRepository;
import com.poshtarenko.codeforge.repository.RespondentRepository;
import com.poshtarenko.codeforge.repository.UserRepository;
import com.poshtarenko.codeforge.security.dto.SignUpRequest;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsServiceImpl;
import com.poshtarenko.codeforge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Profile("integration")
public class TestSecurityUsersInitializer {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthorRepository authorRepository;
    private final RespondentRepository respondentRepository;
    private final UserDetailsServiceImpl userDetailsService;

    private Map<ERole, UserDetailsImpl> userDetailsMap;

    public void setup() {
        if (userDetailsMap != null && !userDetailsMap.isEmpty()) {
            return;
        }

        userDetailsMap = new HashMap<>();
        for (ERole role : ERole.values()) {
            userDetailsMap.put(role, registerUser(role));
        }
    }

    private UserDetailsImpl registerUser(ERole role) {
        String roleName = role.name();

        String email = roleName + getRandomNumber() + "@gmail.com";
        String password = "password";
        String name = "Some " + roleName + " name";

        SignUpRequest authorSignUpRequest = new SignUpRequest(
                email,
                password,
                name,
                role
        );

        userService.register(authorSignUpRequest);
        return (UserDetailsImpl) userDetailsService.loadUserByUsername(email);
    }

    public int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(1000 - 1) + 1;
    }

    public UserDetailsImpl getUserDetails(ERole role) {
        return userDetailsMap.get(role);
    }

    public Author getAuthor() {
        Long id = userDetailsMap.get(ERole.AUTHOR).getId();
        System.out.println("id: " + id);
        return authorRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found"));
    }

    public Respondent getRespondent() {
        Long id = userDetailsMap.get(ERole.RESPONDENT).getId();
        return respondentRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Respondent not found"));
    }

    public User getUser(ERole role) {
        Long id = userDetailsMap.get(role).getId();
        return userRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
