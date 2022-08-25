package com.sparta.instagram.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.instagram.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
@Entity
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //GenerationType.IDENTITY : ID값이 서로 영향없이 자기만의 테이블 기준으로 올라간다.
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    @OnDelete(action = OnDeleteAction.CASCADE) //삭제되어도
    private Comment parent; // 4

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comment> commentbabylist = new ArrayList<>();

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userNic;

    @Column(nullable = false)
    private String userName;

    @ElementCollection
    private Map<String,Boolean> heartmap = new HashMap<>();

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "Article_Id")
    private Article article;
    public void fixCommentMap(String username, boolean userFlag){
        heartmap.replace(username,userFlag);
    }

    public void fixComment(String content) {
        this.content = content;
    }

}

