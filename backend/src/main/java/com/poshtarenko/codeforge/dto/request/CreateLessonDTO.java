package com.poshtarenko.codeforge.dto.request;


import jakarta.validation.constraints.NotBlank;

public record CreateLessonDTO(
        @NotBlank String name
) {
}
