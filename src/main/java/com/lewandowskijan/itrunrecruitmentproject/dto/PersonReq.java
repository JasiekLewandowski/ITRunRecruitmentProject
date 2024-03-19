package com.lewandowskijan.itrunrecruitmentproject.dto;

import com.lewandowskijan.itrunrecruitmentproject.enums.Type;
import com.lewandowskijan.itrunrecruitmentproject.model.validation.ValidPesel;
import com.lewandowskijan.itrunrecruitmentproject.model.validation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.pl.PESEL;

public record PersonReq(
        @NotNull Type type,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @ValidPhone String mobile,
        @NotBlank @Email String email,
        @NotBlank @ValidPesel String pesel

) {
}
