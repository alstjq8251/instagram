package com.sparta.instagram.domain.dto.responsedto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CommentResponseDto {

    private Long id;

    private String content;

    private String userId;

    private String userNic;

    private String userName;

    private Long articleid;

    private long heartcnt;

    private long commentbabycnt;

    private String TimeMsg;

    private boolean userLike;

    private boolean userFlag;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년MM월dd일 HH시mm분ss초", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; //LocalDateTime 시간을 나타내는 자료형

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년MM월dd일 HH시mm분ss초", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

}
