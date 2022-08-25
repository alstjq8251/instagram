package com.sparta.instagram.domain.dto.responsedto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.instagram.domain.Comment;
import com.sparta.instagram.domain.Image;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDto {

    private Long id;

    private String userId;

    private String userNic;

    private String userName;

    private String content;

    private List<Image> imageList;

    private List<String> ImgUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년MM월dd일 HH시mm분ss초", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; //LocalDateTime 시간을 나타내는 자료형

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy년MM월dd일 HH시mm분ss초", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    private long heartcnt;

    private long commentcnt;

    private List<Comment> commentList;

    private String TimeMsg;

    private boolean userLike;

    private boolean userFlag;

}

