package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
@EqualsAndHashCode
public class UserInfo {
    
    private Long id;
    
    @Email(regexp = "(.){4,20}@(.){2,10}\\.(.){2,20}", message = "올바른 형식의 이메일을 입력해주세요.")
    @Max(value = 50, message = "50자 이하만 입력해주세요.")
    private String email;
    
//    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,16}$")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하를 입력해주세요.")
    private String password;
    
//    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,16}$")
    @NotBlank(message = "비밀번호확인을 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호확인은 8자 이상 20자 이하를 입력해주세요.")
    private String rePassword;
    
    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Max(value = 20, message = "닉네임은 최대 20자 입니다.")
    private String nickname;
    
    @NotBlank(message = "소환사명을 입력해 주세요.")
    @Max(value = 20, message = "소환사명은 최대 20자 입니다.")
    private String summoner;
    
}
