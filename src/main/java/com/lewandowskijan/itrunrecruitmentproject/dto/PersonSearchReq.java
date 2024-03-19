package com.lewandowskijan.itrunrecruitmentproject.dto;

import com.lewandowskijan.itrunrecruitmentproject.enums.Type;
import com.lewandowskijan.itrunrecruitmentproject.model.validation.ValidPhone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonSearchReq(
        @NotNull Type type,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @ValidPhone String mobile
) {
}
