package com.commerce.content.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class AddLoginRequest {

    @NotEmpty(message = "아이디는 필수 입니다.")
    private String userId;
    @NotEmpty(message = "비밀번호는 필수 입니다.")
    @Length(min = 8,max = 12,message = "비밀번호는 8자 이상 12자 이하 입니다.")
    private String password;
}
