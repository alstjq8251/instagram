package com.sparta.instagram.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "member")
//@Document(indexName = "hashtag") // 엘라스틱 서치를 위해 등록하는것 RDB에선 @Entity와 똑같으며 중복사용가능 엘라스틱 DB
@Entity // RDS
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "userid")
    private String userId;

    @Column(nullable = false, unique = true, name = "usernic")
    private String userNic;
    //
    @Column(nullable = false, name ="username")
    private String userName;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

//    @Transient
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String userId, String userNic, String userName, String password, Authority authority) {
        this.userId = userId;
        this.userNic = userNic;
        this.userName = userName;
        this.password = password;
        this.authority = authority;
    }
}


