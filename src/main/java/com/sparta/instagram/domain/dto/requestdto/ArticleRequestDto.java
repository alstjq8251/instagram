package com.sparta.instagram.domain.dto.requestdto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequestDto {

    @ApiModelProperty(example = "게시글 request당시 입력할 내용 예외처리를 위해 유효성검사 어노테이션 미적용")
    //@NotBlank
    private String content;
}
