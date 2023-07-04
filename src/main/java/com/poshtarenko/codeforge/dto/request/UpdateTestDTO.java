package com.poshtarenko.codeforge.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record UpdateTestDTO(
        @JsonIgnore Long id,
        @NotBlank String name,
        @Positive Integer maxDuration) {
}
