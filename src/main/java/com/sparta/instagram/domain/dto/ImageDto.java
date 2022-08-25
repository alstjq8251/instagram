package com.sparta.instagram.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDto {
        private String uploadImageUrl;
        private String imageName;


        @Builder
        public ImageDto(String uploadImageUrl, String imageName) {
            this.uploadImageUrl = uploadImageUrl;
            this.imageName = imageName;

    }

}
