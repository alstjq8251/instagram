package com.sparta.instagram.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.instagram.Timestamped;
import com.sparta.instagram.domain.dto.requestdto.ArticleRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "article")
public class Article extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userNic;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean userLike;

    @ElementCollection
    private Map<String,Boolean> heartmap = new HashMap<>();

    @OneToMany(mappedBy = "article") //게시글에 달린 댓글 연관관계 매핑
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "article")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Image> imageList;

    public void update(ArticleRequestDto articleRequestDto){
        this.content = articleRequestDto.getContent();
    }

    public void deleteImage(Image image){
        this.imageList.remove(image);
    }

    public void fixHeartMap(String username, boolean userFlag){
        heartmap.replace(username,userFlag);
    }

    @Builder
    public Article(String userId, String userNic,String userName,boolean userLike,String content,Map<String,Boolean> heartmap){
        this.userId=userId;
        this.userNic=userNic;
        this.userName=userName;
        this.userLike=userLike;
        this.content=content;
        this.heartmap = heartmap;
    }
}

