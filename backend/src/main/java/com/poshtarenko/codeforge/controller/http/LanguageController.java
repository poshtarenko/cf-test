package com.poshtarenko.codeforge.controller.http;

import com.poshtarenko.codeforge.dto.response.ViewLanguageDTO;
import com.poshtarenko.codeforge.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/languages")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping
    public List<ViewLanguageDTO> findAll() {
        return languageService.findAll();
    }
}
