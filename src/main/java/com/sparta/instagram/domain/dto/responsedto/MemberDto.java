package com.sparta.instagram.domain.dto.responsedto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {


    private Long id;

    private String userId;


    private String userNic;

    private String userName;

    private String TimeMsg;
}
