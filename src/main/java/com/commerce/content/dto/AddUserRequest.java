package com.commerce.content.dto;

import com.commerce.content.domain.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter @Setter
public class AddUserRequest {

    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String userName;
    @NotEmpty(message = "아이디는 필수 입니다.")
    private String userId;
    @NotEmpty(message = "비밀번호는 필수 입니다.")
    @Length(min = 8,max = 12,message = "비밀번호는 8자 이상 12자 이하 입니다.")
    private String password;
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$")
    private String phoneNumber;

    private String state;
    private String city;
    private String street;
    private String zipcode;
}
