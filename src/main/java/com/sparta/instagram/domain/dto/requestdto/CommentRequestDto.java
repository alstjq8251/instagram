package com.sparta.instagram.domain.dto.requestdto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CommentRequestDto {

    private String content;
}