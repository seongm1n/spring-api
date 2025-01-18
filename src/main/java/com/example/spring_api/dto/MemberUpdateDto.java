package com.example.spring_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberUpdateDto {
    @NotEmpty(message = "이름은 필수입니다")
    private String name;

    @Email(message = "이메일 형식이 올바르지 않습니다")
    @NotEmpty(message = "이메일은 필수입니다")
    private String email;
}