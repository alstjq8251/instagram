package com.sparta.instagram.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String ImgUrl;

    @Column
    private String ImgName;

    @ManyToOne
    @JsonBackReference
    private Article article;

    @Builder
    public Image(String ImgUrl, String ImgName,Article article ){
        this.ImgUrl=ImgUrl;
        this.ImgName=ImgName;
        this.article=article;
    }
}
