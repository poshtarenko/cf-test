package com.poshtarenko.codeforge.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record SaveProblemDTO(
        @NotBlank String name,
        @NotBlank String description,
        @Positive Long languageId,
        @Positive Long categoryId,
        @NotBlank String testingCode) {
}
