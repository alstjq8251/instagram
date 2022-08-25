package com.sparta.instagram.domain;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
////import org.springframework.data.elasticsearch.annotations.Document;
//
//import javax.persistence.*;
////import javax.transaction.Transactional;
//
//@Getter
//@NoArgsConstructor
//@Entity
//public class Heart {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) //GenerationType.IDENTITY : ID값이 서로 영향없이 자기만의 테이블 기준으로 올라간다.
//    @ApiModelProperty(example = "좋아요 아이디")
//    private Long id;
//
//    @ApiModelProperty(example = "작성자 이메일")
//    @Column(nullable = false)
//    private String userId;
//
//    @ApiModelProperty(example = "연관된 게시글")
//    @JoinColumn(name = "Article_Id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonBackReference
//    private Article article;
//
//    @ApiModelProperty(example = "연관된 댓글")
//    @JoinColumn(name = "Comment_Id")
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonBackReference
//    private Comment comment;
//
//    @Builder
//    public Heart(String userId, Article article, Comment comment){
//        this.article= article;
//        this.comment = comment;
//        this.userId = userId;
//    }
//}