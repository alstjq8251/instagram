package com.sparta.instagram.domain.dto.responsedto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HeartResponseDto {

    long likeCount;
    boolean likeFlag;
}
