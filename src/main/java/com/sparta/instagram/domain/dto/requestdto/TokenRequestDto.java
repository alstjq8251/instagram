package com.sparta.instagram.domain.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestDto {

    private String accessToken;
    private String refreshToken;
}
