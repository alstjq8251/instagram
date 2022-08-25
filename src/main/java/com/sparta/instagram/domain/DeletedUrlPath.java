package com.sparta.instagram.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class DeletedUrlPath {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String deletedUrlPath;

    @Builder
    public DeletedUrlPath(String deletedUrlPath){
        this.deletedUrlPath=deletedUrlPath;
    }



}

