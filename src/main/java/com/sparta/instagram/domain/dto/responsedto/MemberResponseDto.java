package com.sparta.instagram.domain.dto.responsedto;

import com.sparta.instagram.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String userId;

    public static MemberResponseDto of(Member member) {
        return new MemberResponseDto(member.getUserId());
    }
}
