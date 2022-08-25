package com.sparta.instagram.domain.dto;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class S3Dto {
    String fileName;
    String uploadImageUrl;
}

