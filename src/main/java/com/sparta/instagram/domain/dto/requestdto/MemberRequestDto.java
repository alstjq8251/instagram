package com.sparta.instagram.domain.dto.requestdto;


import com.sparta.instagram.domain.Authority;
import com.sparta.instagram.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    private String userId;

    private String userNic; // 이름

    private String userName; // 별명

    private String password; // 비번

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .userId(userId)
                .userNic(userNic)
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .authority(Authority.ROLE_USER)
                .build();
    }


    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userId, password);
    }
}
