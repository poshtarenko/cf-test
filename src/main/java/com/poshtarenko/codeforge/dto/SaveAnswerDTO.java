package com.poshtarenko.codeforge.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record SaveAnswerDTO(
        String code,
        Long taskId,
        @JsonIgnore Long respondentId) {
}
